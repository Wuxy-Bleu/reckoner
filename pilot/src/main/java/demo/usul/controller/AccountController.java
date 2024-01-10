package demo.usul.controller;

import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountModifyRecordDto;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.pojo.AccountListResponse;
import demo.usul.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// with cache
@Validated
@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<AccountListResponse> retrieveActivatedByConditionsOrNot(
            @RequestParam(required = false) Optional<String> type,
            @RequestParam(required = false) Optional<String> currency) {
        List<AccountDto> res = accountService.retrieveActivatedByConditionsOrNot(type, currency);
        return ResponseEntity.ok(AccountListResponse.builder().count(res.size()).accounts(res).build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<AccountDto> retrieveActivatedByName(@PathVariable @NotBlank String name) {
        return ResponseEntity.ok(accountService.retrieveActivatedByName(name));
    }

    @GetMapping("/modify-history/{uuid}")
    public ResponseEntity<List<AccountModifyRecordDto>> retrieveModifyHistory(@PathVariable @NotBlank String uuid) {
        return ResponseEntity.ok(
                accountService.retrieveModifyHistory(uuid));
    }

    @PostMapping()
    public ResponseEntity<AccountDto> createOne(@RequestBody @Valid AccountDto accountDto) {
        return ResponseEntity.ok(accountService.create(accountDto));
    }

    @DeleteMapping()
    public Integer delete(@RequestBody List<UUID> delIds) {
        return accountService.softDelete(delIds);
    }

    @DeleteMapping("/{id}")
    public Integer deleteById(@PathVariable @NotNull UUID id) {
        return accountService.softDelete(List.of(id));
    }

    @PutMapping()
    public List<AccountDto> update(@RequestBody List<AccountUpdateDto> accountUpdateDtos) {
        return accountService.updateAndReturnDtos(accountUpdateDtos);
    }

}
