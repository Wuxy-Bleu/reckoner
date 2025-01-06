package demo.usul.controller;

import demo.usul.beans.CachedAcctsDto;
import demo.usul.dto.AccountDto;
import demo.usul.service.CacheReactiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/cache/v4")
public class CacheController {

    private final CacheReactiveService cacheReactiveService;

    @Autowired
    public CacheController(CacheReactiveService cacheReactiveService) {
        this.cacheReactiveService = cacheReactiveService;
    }

    @PostMapping("/{ms}")
    public Mono<Object> cacheAccounts(@RequestBody List<AccountDto> accounts, @PathVariable Long ms) {
        return cacheReactiveService.cacheAccountsReactive(accounts, ms);
    }

    // 不要用feign发送optional obj, 不要在feign接口使用optional, web layer还是可以使用optional的，
    // feign和controller之间还有spring做了一层预处理
    @GetMapping
    public Mono<CachedAcctsDto> getCachedAccounts(@RequestParam(required = false) Optional<String> name,
                                                  @RequestParam(required = false) Optional<String> cardType,
                                                  @RequestParam(required = false) Optional<String> currency) {
        return cacheReactiveService.getCachedAccounts(name, cardType, currency);
    }

    @GetMapping("/{id}")
    public Mono<CachedAcctsDto> getById(@PathVariable UUID id) {
        return cacheReactiveService.getCachedAcctById(id);
    }
}
