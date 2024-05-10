package demo.usul.service;

import demo.usul.convert.AccountMapper;
import demo.usul.convert.ReckonerMapper;
import demo.usul.dto.AccountDto;
import demo.usul.dto.AcctBlcCalculateDto;
import demo.usul.dto.ReckonerDto;
import demo.usul.entity.ReckonerEntity;
import demo.usul.feign.AcctBlcCalFeign;
import demo.usul.repository.AccountRepository;
import demo.usul.repository.ReckonerRepository;
import demo.usul.repository.fragments.ReckonerFragRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class RcknSvcV2 {

    private final ReckonerRepository reckonerRepository;
    private final ReckonerMapper reckonerMapper;
    private final AcctBlcCalFeign acctBlcCalFeign;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    @Autowired
    public RcknSvcV2(ReckonerRepository reckonerRepository, ReckonerMapper reckonerMapper, AcctBlcCalFeign acctBlcCalFeign, AccountService accountService, AccountMapper accountMapper, AccountRepository accountRepository) {
        this.reckonerRepository = reckonerRepository;
        this.reckonerMapper = reckonerMapper;
        this.acctBlcCalFeign = acctBlcCalFeign;
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    public Map<String, Object> retrieveOneInfo(UUID id) {
        ReckonerEntity entity = reckonerRepository.findById(id).orElseThrow();
        AcctBlcCalculateDto acctBlcCalculateDto = acctBlcCalFeign.retrieveOne(id);

        Map<String, Object> res = new HashMap<>();
        res.put("reckoner", reckonerMapper.reckonerEntity2Dto(entity));
        res.put("mongo-record", acctBlcCalculateDto);
        return res;
    }

    /**
     * 不会update mongo document，只是add new document
     *
     * @param id   reckoner to be updated
     * @param name new f_acct name
     * @return new reckoner
     */
    @Transactional
    public ReckonerDto updateFAcct(UUID id, String name) {
        ReckonerEntity reckoner = reckonerRepository.findById(id).orElseThrow();
        AccountDto newAcc = accountService.getOrRefreshCache(null, name, null, null).get(0);
        AccountDto oldAcc = accountService.getOrRefreshCache(null, reckoner.getFromAcctObj().getName(), null, null).get(0);

        BigDecimal newAccBlcAfter = newAcc.getBalance().subtract(reckoner.getAmount().abs());
        BigDecimal oldAccBlcAfter = oldAcc.getBalance().add(reckoner.getAmount().abs());
        int i = reckonerRepository.updateFromAcctById(newAcc.getId(), id);
        int i1 = accountRepository.updateBalanceByIdAndIsActive(newAccBlcAfter, newAcc.getId(), true);
        int i2 = accountRepository.updateBalanceByIdAndIsActive(oldAccBlcAfter, oldAcc.getId(), true);

        acctBlcCalFeign.saveOne(
                AcctBlcCalculateDto.builder()
                        .acctId(newAcc.getId())
                        .diff(reckoner.getAmount().negate())
                        .beforeBlc(newAcc.getBalance())
                        .acctName(newAcc.getName())
                        .afterBlc(newAccBlcAfter)
                        .transDate(OffsetDateTime.now())
                        .build());
        acctBlcCalFeign.saveOne(
                AcctBlcCalculateDto.builder()
                        .acctId(oldAcc.getId())
                        .diff(reckoner.getAmount())
                        .beforeBlc(oldAcc.getBalance())
                        .acctName(oldAcc.getName())
                        .afterBlc(oldAccBlcAfter)
                        .transDate(OffsetDateTime.now())
                        .build()
        );

        return reckonerMapper.reckonerEntity2Dto(reckonerRepository.findById(id).orElseThrow());
    }

    public Map<String, List<ReckonerFragRepositoryImpl.Stat>> statsToAcc(String name) {
        AccountDto toAcc = accountService.getOrRefreshCache(null, name, null, null).get(0);
        return reckonerRepository.statsToAcc(toAcc.getId());
    }

    public Map<String, List<ReckonerFragRepositoryImpl.Stat>> statsFromAcc(String name) {
        AccountDto fromAcc = accountService.getOrRefreshCache(null, name, null, null).get(0);
        return reckonerRepository.statsFromAcc( fromAcc.getId());
    }
}