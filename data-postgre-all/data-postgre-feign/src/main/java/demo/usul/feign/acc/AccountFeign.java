package demo.usul.feign.acc;

import demo.usul.feign.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "postgresClient")
public interface AccountFeign {

    @GetMapping
    List<AccountDto> retrieveActivatedByConditionsOrNot(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String currency);

    @GetMapping("/{name}")
    AccountDto retrieveActivatedByName(@PathVariable String name);


    @PostMapping()
    AccountDto createOne(@RequestBody AccountDto accountDto);

    @DeleteMapping("/{id}")
    void deleteOne(@PathVariable UUID id);
}
