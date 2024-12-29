package demo.usul.service;

import demo.usul.dto.AccountDto;
import demo.usul.entity.AccountEntity;
import demo.usul.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AcctAggregationSvc {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public AcctAggregationSvc(AccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public Map<String, String> allMyMoney() {
        List<AccountDto> accts = accountService.getOrRefreshCache(null, null, null, null);
        Map<String, List<AccountDto>> collect = accts.stream().collect(Collectors.groupingBy(AccountDto::getCardType));

        Map<String, String> res = new HashMap<>();
        for (Map.Entry<String, List<AccountDto>> e : collect.entrySet()) {
            res.put(e.getKey(),
                    String.valueOf(e.getValue().stream()
                            .map(AccountDto::getBalance) // Extract the BigDecimal field
                            .reduce(BigDecimal.ZERO, BigDecimal::add)));
        }
        res.put("Total Assets", String.valueOf(accts.stream().map(AccountDto::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add)));
        return res;
    }

    public void groupByBillingCycle() {
        List<AccountEntity> allAccounts = accountRepository.findByIsActiveTrueOrderByBalanceDesc();
    }
}
