package demo.usul.controller;

import demo.usul.entity.ReckonerEntity;
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

    @GetMapping("/count/{fromAcct}")
    public Long countByFromAcct(@PathVariable UUID fromAcct) {
        return reckonerService.countDistinctByFromAcctAllIgnoreCase(fromAcct);
    }

    @GetMapping("/{name}")
    public List<ReckonerEntity> retrieveByName(@PathVariable String name) {
        return reckonerService.retrieveByName(name);
    }

    @GetMapping("")
    public List<ReckonerEntity> retrieveAll(){
        return reckonerService.retrieveAll();
    }
}
