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

@FeignClient(name = "redisson-client", configuration = {OptionalSniffer.class})
public interface CacheFeign {

    @GetMapping("/v3/cache/accts")
    CachedAcctsDto getCachedAccts(@RequestParam(required = false) String name, @RequestParam(required = false) String cardType, @RequestParam(required = false) String currency);

    @PostMapping("/v3/cache/{ms}")
    void cacheAccounts(@PathVariable Long ms, @RequestBody List<AccountDto> accts);

    @GetMapping("/cache/accts/v3/{id}")
    CachedAcctsDto getCachedAcctById(@PathVariable String id);


}
