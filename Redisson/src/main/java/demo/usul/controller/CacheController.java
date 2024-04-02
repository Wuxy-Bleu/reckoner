package demo.usul.controller;

import demo.usul.dto.AccountDto;
import demo.usul.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v2/cache")
@Deprecated
public class CacheController {

    private final CacheService cacheService;

    @Autowired
    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/accounts/{ms}")
    public void cacheAccounts(@RequestBody List<AccountDto> accounts, @PathVariable Long ms) {
        cacheService.cacheAccounts(accounts, ms);
    }
}
