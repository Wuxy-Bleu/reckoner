package demo.usul.service;

import demo.usul.consta.Constants;
import demo.usul.convert.AccountMapper;
import demo.usul.convert.ReckonerMapper;
import demo.usul.dto.AccountDto;
import demo.usul.dto.AcctBlcCalculateDto;
import demo.usul.dto.ReckonerDto;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.feign.AcctBlcCalFeign;
import demo.usul.feign.CacheFeign;
import demo.usul.repository.ReckonerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    private final CacheFeign cacheFeign;

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

    public List<ReckonerEntity> retrieveByToAcctName(String name) {
        AccountEntity acct = accountService.retrieveActivatedByName(name);
        return reckonerRepository.findByToAcct(acct.getId(), Pageable.ofSize(10));
    }

    public List<ReckonerEntity> retrieveAll() {
        return reckonerRepository.findAll();
    }

    /**
     * will trigger accts balance changes
     *
     * @param reckoner record will be inserted, child entity必须存在，且提供name, 只提供id不行
     * @return 从db中读取的被插入的record
     */
    public ReckonerDto createOne(ReckonerDto reckoner) {
        ReckonerEntity entity = reckonerMapper.reckonerDto2Entity(reckoner);

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

        switch (entity.getInOut()) {
            case 0:
                Objects.requireNonNull(fAcct).blcSubtract(entity.getAmount().abs());
                Objects.requireNonNull(tAcct).blcAdd(entity.getAmount().abs());
                acctBlcCalFeign.saveOne(new AcctBlcCalculateDto()
                        .acctId(fAcct.getId())
                        .afterBlc(fAcct.getBalance())
                        .transDate(entity.getTransDate())
                        .afterRekn(entity.getId()));
                acctBlcCalFeign.saveOne(new AcctBlcCalculateDto()
                        .transDate(entity.getTransDate())
                        .acctId(tAcct.getId())
                        .afterRekn(entity.getId())
                        .afterBlc(tAcct.getBalance()));
                cacheFeign.cacheAccounts(Constants.ACCTS_CACHE_TTL_MS, List.of(fAcct, tAcct));
                break;
            case 1:
                Objects.requireNonNull(tAcct).blcAdd(entity.getAmount().abs());
                acctBlcCalFeign.saveOne(new AcctBlcCalculateDto()
                        .transDate(entity.getTransDate())
                        .acctId(tAcct.getId())
                        .afterRekn(entity.getId())
                        .afterBlc(tAcct.getBalance()));
                cacheFeign.cacheAccounts(Constants.ACCTS_CACHE_TTL_MS, List.of(tAcct));
                break;
            case -1:
                Objects.requireNonNull(fAcct).blcSubtract(entity.getAmount().abs());
                acctBlcCalFeign.saveOne(new AcctBlcCalculateDto()
                        .acctId(fAcct.getId())
                        .afterBlc(fAcct.getBalance())
                        .transDate(entity.getTransDate())
                        .afterRekn(entity.getId()));
                cacheFeign.cacheAccounts(Constants.ACCTS_CACHE_TTL_MS, List.of(fAcct));
                break;
            default:
                break;
        }

        cacheFeign.cacheAccounts(Constants.ACCTS_CACHE_TTL_MS, List.of());

        return reckonerMapper.reckonerEntity2Dto(reckonerRepository.findById(entity.getId()).orElseThrow());
    }
}
