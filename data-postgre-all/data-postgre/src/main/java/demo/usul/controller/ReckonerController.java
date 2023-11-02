package demo.usul.controller;

import demo.usul.convert.ReckonerMapper;
import demo.usul.feign.dto.ReckonerDto;
import demo.usul.service.ReckonerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

// 问题已解决，这就用来测试事务传播吧
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/reckoner")
public class ReckonerController {

    private final ReckonerService reckonerService;

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
}
