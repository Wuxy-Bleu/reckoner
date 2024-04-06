package demo.usul.feign;

import demo.usul.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "redisson-client")
public interface CacheFeign {

    @GetMapping("/v3/cache/accts")
    List<AccountDto> getCachedAccts(@RequestParam Optional<String> type, @RequestParam Optional<String> currency);

    @PostMapping("/v3/cache/{ms}")
    void cacheAccounts(@PathVariable Long ms, @RequestBody List<AccountDto> accts);
}
