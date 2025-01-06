package demo.usul.feign;

import demo.usul.beans.CachedAcctsDto;
import demo.usul.dto.AccountDto;
import demo.usul.interceptor.OptionalSniffer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

// feign 和 controller返回值不同 controller包裹了一层mono
@FeignClient(name = "redisson-client", configuration = {OptionalSniffer.class})
public interface CacheFeign {

    @PostMapping("/{ms}")
    Object cacheAccounts(@RequestBody List<AccountDto> accounts, @PathVariable Long ms);

    @GetMapping
    CachedAcctsDto getCachedAccounts(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) String cardType,
                                     @RequestParam(required = false) String currency);

    @GetMapping("/{id}")
    CachedAcctsDto getById(@PathVariable UUID id);
}

