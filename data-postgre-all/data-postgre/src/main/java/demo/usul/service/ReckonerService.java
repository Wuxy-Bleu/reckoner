package demo.usul.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import demo.usul.convert.AccountMapper;
import demo.usul.convert.ReckonerMapper;
import demo.usul.dto.AccountDto;
import demo.usul.dto.AcctBlcCalculateDto;
import demo.usul.dto.ReckonerDto;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.feign.AcctBlcCalFeign;
import demo.usul.repository.ReckonerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReckonerService {

    private final ReckonerRepository reckonerRepository;
    private final AccountService accountService;
    private final ReckonerMapper reckonerMapper;
    private final AccountMapper accountMapper;
    private final AcctBlcCalFeign acctBlcCalFeign;

    public Long countDistinctByFromAcctAllIgnoreCase(UUID acct) {
        return reckonerRepository.countDistinctByFromAcctAllIgnoreCase(acct);
    }

    public Long countDistinctByToAcctAllIgnoreCase(UUID acct) {
        return reckonerRepository.countDistinctByToAcctAllIgnoreCase(acct);
    }

    public List<ReckonerEntity> retrieveByFromAcctName(String name) {
        AccountEntity acct = accountService.retrieveActivatedByName(name);
        return reckonerRepository.findByFromAcct(acct.getId(), Pageable.ofSize(10));
    }

    public List<ReckonerEntity> retrieveByFromAcctNameAndTags(String name, List<String> tags) {
        AccountEntity acct = accountService.retrieveActivatedByName(name);
        return reckonerRepository.findByFromAcctAndTagsOrderByTransDateDesc(acct.getId(),
                ".*" + String.join(".*", tags) + ".*",
                ".*" + String.join(".*", CollUtil.reverse(tags)) + ".*");
    }

    public List<ReckonerEntity> retrieveByTags(List<String> tags) {
        return reckonerRepository.findByTagsOrderByTransDateDesc(
                ".*" + String.join(".*", tags) + ".*",
                ".*" + String.join(".*", CollUtil.reverse(tags)) + ".*");
    }

    public Page<ReckonerEntity> retrieveByToAcctName(String name, Pageable page) {
        AccountDto toAcc = accountService.getOrRefreshCache(null, name, null, null).get(0);
        return reckonerRepository.findByToAcctOrderByInOutAscTypeIdAscTransDateDesc(toAcc.getId(), page);
    }

    public List<ReckonerEntity> retrieveAll() {
        return reckonerRepository.findAll();
    }

    public ReckonerDto retrieveById(UUID id) {
        return reckonerMapper.reckonerEntity2Dto(reckonerRepository.findById(id).orElseThrow());
    }

    /**
     * todo 得想办法在service层解决事务的问题
     */
    public ReckonerDto createOneWithTrigger(ReckonerDto dto, boolean trigger) {
        ReckonerEntity entity = createOne(dto, trigger);
        accountService.refreshCache();
        return reckonerMapper.reckonerEntity2Dto(reckonerRepository.findById(entity.getId()).orElseThrow());
    }

    /**
     * will trigger accts balance changes
     *
     * @param reckoner record will be inserted, child entity必须存在，且必须提供name, 只提供id不行
     * @return 从db中读取的被插入的record
     */
    @Transactional
    public ReckonerEntity createOne(ReckonerDto reckoner, boolean trigger) {
        ReckonerEntity entity = reckonerMapper.reckonerDto2Entity(reckoner);
        if (ObjectUtil.isEmpty(entity.getTransDate())) {
            entity.setTransDate(OffsetDateTime.now());
        }

        AccountDto fAcct = null;
        AccountDto tAcct = null;
        // from cache
        switch (entity.getInOut()) {
            case 0:
                fAcct = accountService.getOrRefreshCache(null, entity.getFromAcctObj().getName(), null, null).get(0);
                tAcct = accountService.getOrRefreshCache(null, entity.getToAcctObj().getName(), null, null).get(0);
                entity.setFromAcct(fAcct.getId());
                entity.setFromAcctObj(accountMapper.accountDto2Entity(fAcct));
                entity.setToAcct(tAcct.getId());
                entity.setToAcctObj(accountMapper.accountDto2Entity(tAcct));
                break;
            case 1:
                tAcct = accountService.getOrRefreshCache(null, entity.getToAcctObj().getName(), null, null).get(0);
                entity.setToAcct(tAcct.getId());
                entity.setToAcctObj(accountMapper.accountDto2Entity(tAcct));
                break;
            case -1:
                fAcct = accountService.getOrRefreshCache(null, entity.getFromAcctObj().getName(), null, null).get(0);
                entity.setFromAcct(fAcct.getId());
                entity.setFromAcctObj(accountMapper.accountDto2Entity(fAcct));
                break;
            default:
                break;
        }

        reckonerRepository.saveAssociations(entity);

        // update accts
        if (trigger) {
            switch (entity.getInOut()) {
                case 0: {
                    assert fAcct != null;
                    assert tAcct != null;
                    AcctBlcCalculateDto.AcctBlcCalculateDtoBuilder fAcctBuilder = AcctBlcCalculateDto.builder()
                            .acctId(fAcct.getId())
                            .acctName(fAcct.getName())
                            .diff(entity.getAmount().abs().negate())
                            .beforeBlc(fAcct.getBalance())
                            .transDate(entity.getTransDate())
                            .afterRekn(entity.getId());
                    AcctBlcCalculateDto.AcctBlcCalculateDtoBuilder tAcctBuilder = AcctBlcCalculateDto.builder()
                            .acctId(tAcct.getId())
                            .acctName(tAcct.getName())
                            .diff(entity.getAmount().abs())
                            .beforeBlc(tAcct.getBalance())
                            .transDate(entity.getTransDate())
                            .afterRekn(entity.getId());
                    fAcct.blcSubtract(entity.getAmount().abs());
                    tAcct.blcAdd(entity.getAmount().abs());
                    acctBlcCalFeign.saveOne(fAcctBuilder.afterBlc(fAcct.getBalance()).build());
                    acctBlcCalFeign.saveOne(tAcctBuilder.afterBlc(tAcct.getBalance()).build());
                    accountService.updateBlc(List.of(fAcct, tAcct));
                    break;
                }
                case 1: {
                    assert tAcct != null;
                    AcctBlcCalculateDto.AcctBlcCalculateDtoBuilder tAcctBuilder = AcctBlcCalculateDto.builder()
                            .acctId(tAcct.getId())
                            .acctName(tAcct.getName())
                            .diff(entity.getAmount().abs())
                            .beforeBlc(tAcct.getBalance())
                            .transDate(entity.getTransDate())
                            .afterRekn(entity.getId());
                    tAcct.blcAdd(entity.getAmount().abs());
                    acctBlcCalFeign.saveOne(tAcctBuilder.afterBlc(tAcct.getBalance()).build());
                    accountService.updateBlc(List.of(tAcct));
                    break;
                }
                case -1: {
                    assert fAcct != null;
                    AcctBlcCalculateDto.AcctBlcCalculateDtoBuilder fAcctBuilder = AcctBlcCalculateDto.builder()
                            .acctId(fAcct.getId())
                            .acctName(fAcct.getName())
                            .diff(entity.getAmount().abs().negate())
                            .beforeBlc(fAcct.getBalance())
                            .transDate(entity.getTransDate())
                            .afterRekn(entity.getId());
                    fAcct.blcSubtract(entity.getAmount().abs());
                    acctBlcCalFeign.saveOne(fAcctBuilder.afterBlc(fAcct.getBalance()).build());
                    accountService.updateBlc(List.of(fAcct));
                    break;
                }
                default:
                    break;
            }
        }
        return entity;

    }

    public ReckonerDto latestCreated() {
        return reckonerMapper.reckonerEntity2Dto(reckonerRepository.findFirstByIsAliveOrderByCreatedAtDesc(true));
    }

    public ReckonerDto latestTransaction() {
        return reckonerMapper.reckonerEntity2Dto(reckonerRepository.findFirstByIsAliveOrderByTransDateDesc(true));
    }

    public void deleteById(UUID id) {
        reckonerRepository.deleteById(id);
    }

}
