package demo.usul.controller;

import demo.usul.feign.dto.AccountDto;
import demo.usul.mvc.RequestContextUtils;
import demo.usul.pojo.AccountListResponse;
import demo.usul.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

// with cache
@Validated
@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final Environment environment;

    @GetMapping
    public ResponseEntity<AccountListResponse> retrieveActivatedByConditionsOrNot(
            @RequestParam(required = false) Optional<String> type,
            @RequestParam(required = false) Optional<String> currency) {


        List<AccountDto> res = accountService.retrieveActivatedByConditionsOrNot(type, currency).orElse(Collections.emptyList());
        return ResponseEntity.ok(AccountListResponse.builder().count(res.size()).accounts(res).build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<AccountDto> retrieveActivatedByName(@PathVariable @NotBlank String name) {
        return ResponseEntity.ok(accountService.retrieveActivatedByName(name));
    }

    @PostMapping()
    public ResponseEntity<AccountDto> createOne(@RequestBody @Valid AccountDto accountDto) {
        return ResponseEntity.ok(accountService.create(accountDto));
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable @NotBlank UUID id) {
        accountService.deleteOne(id);
    }

    @GetMapping("test")
    public String test(HttpServletRequest request) {
        String property = environment.getProperty("data-postgre.greeting");
        Locale attrsLang = RequestContextUtils.getAttrsLang();
        return null;
    }
}
