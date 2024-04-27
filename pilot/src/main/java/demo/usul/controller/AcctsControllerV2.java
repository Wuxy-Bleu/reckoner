package demo.usul.controller;

import demo.usul.converter.AccountMapper;
import demo.usul.dto.AccountDto;
import demo.usul.pojo.AcctsVo;
import demo.usul.service.AccountService;
import demo.usul.service.AcctServiceV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.empty;

@Slf4j
@RestController
@RequestMapping("/accts/v2")
public class AcctsControllerV2 {

    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final AcctServiceV2 acctServiceV2;

    public AcctsControllerV2(AccountMapper accountMapper, AccountService accountService, AcctServiceV2 acctServiceV2) {
        this.accountMapper = accountMapper;
        this.accountService = accountService;
        this.acctServiceV2 = acctServiceV2;
    }

    @GetMapping()
    public Map<String, Object> cleanerAcctsRecord() {
        List<AccountDto> accountDtos = accountService.retrieveActivatedByConditionsOrNot(empty(), empty());
        Map<String, List<AcctsVo>> collect = accountDtos.stream()
                .map(accountMapper::acctDto2VO)
                .sorted(Comparator.comparing(dto -> dto.getBalance().abs()))
                .collect(Collectors.groupingBy(AcctsVo::getCardType));

        Map<String, Object> res = new HashMap<>();
        res.put("Total-Count", accountDtos.size());
        for (Map.Entry<String, List<AcctsVo>> entry : collect.entrySet()) {
            res.put(entry.getKey() + " -> " + entry.getValue().size(), entry.getValue());
        }

        res.putAll(acctServiceV2.assets());
        return res;
    }
}
