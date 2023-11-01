package demo.usul.feign.acc;

import demo.usul.feign.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "postgresClient")
public interface AccountFeign {

    @GetMapping
    List<AccountDto> retrieveActivatedByConditionsOrNot(
            @RequestParam(required = false) Optional<String> type,
            @RequestParam(required = false) Optional<String> currency);
}
