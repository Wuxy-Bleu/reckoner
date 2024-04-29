package demo.usul.controller;

import demo.usul.convert.ReckonerMapper;
import demo.usul.dto.AccountDto;
import demo.usul.dto.ReckonerDto;
import demo.usul.dto.ReckonerTypeDto;
import demo.usul.entity.ReckonerEntity;
import demo.usul.enums.InOutEnum;
import demo.usul.service.AccountService;
import demo.usul.service.ReckonerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/reckoner")
public class ReckonerController {

    private final ReckonerService reckonerService;
    private final AccountService accountService;
    private final ReckonerMapper reckonerMapper;

    @GetMapping("/count/from/{acct}")
    public Long countByFromAcct(@PathVariable UUID acct) {
        return reckonerService.countDistinctByFromAcctAllIgnoreCase(acct);
    }

    @GetMapping("/count/to/{acct}")
    public Long countByToAcct(@PathVariable UUID acct) {
        return reckonerService.countDistinctByToAcctAllIgnoreCase(acct);
    }

    @GetMapping("/from/{name}")
    public List<ReckonerDto> retrieveByFromAcctName(@PathVariable String name) {
        return reckonerMapper.reckonerEntities2Dtos(reckonerService.retrieveByFromAcctName(name));
    }

    @GetMapping("/to/{name}")
    public List<ReckonerDto> retrieveByToAcctName(@PathVariable String name) {
        return reckonerMapper.reckonerEntities2Dtos(reckonerService.retrieveByToAcctName(name));
    }

    @GetMapping("")
    public List<ReckonerDto> retrieveAll() {
        return reckonerMapper.reckonerEntities2Dtos(reckonerService.retrieveAll());
    }

    @PostMapping("")
    public ReckonerDto createOne(@RequestBody @Valid ReckonerDto reckoner, @RequestParam(required = false) Optional<Boolean> trigger) {
        ReckonerEntity entity = reckonerService.createOne(reckoner, trigger.orElse(true));
        accountService.refreshCache();
        return reckonerService.retrieveById(entity.getId());
    }

    @PostMapping("/yuebao-profit/{blc}")
    public ReckonerDto createYuEBaoProfit(@PathVariable String blc, @RequestBody OffsetDateTime transDate) {
        ReckonerDto build = ReckonerDto.builder()
                .inOut(InOutEnum.IN)
                .amount(new BigDecimal(blc))
                .transDate(transDate)
                .toAcctObj(AccountDto.builder().name("余额宝").build())
                .reckonerTypeObj(ReckonerTypeDto.builder().typeName("Profit").build())
                .build();
        return createOne(build, Optional.of(true));
    }

}
