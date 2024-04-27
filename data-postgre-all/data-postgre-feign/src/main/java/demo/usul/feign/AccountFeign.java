package demo.usul.feign;

import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.fallback.AccountFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "postgresClient", fallbackFactory = AccountFeignFallbackFactory.class)
public interface AccountFeign {

    @GetMapping
    List<AccountDto> retrieveActivatedByConditionsOrNot(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String currency);

    @GetMapping("/{name}")
    AccountDto retrieveActivatedByName(@PathVariable String name);


    @PostMapping()
    AccountDto createOne(@RequestBody AccountDto accountDto);

    @DeleteMapping()
    Integer delete(@RequestBody List<UUID> delIds);

    @DeleteMapping("/{id}")
    Integer deleteById(@PathVariable UUID id);

    @PutMapping()
    List<AccountDto> update(@RequestBody List<AccountUpdateDto> accountUpdateDtos);

    @GetMapping("/assets")
    Map<String, String> allMyMoney();
}
