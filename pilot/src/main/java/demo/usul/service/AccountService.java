package demo.usul.service;

import demo.usul.feign.acc.AccountFeign;
import demo.usul.feign.dto.AccountDto;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountFeign accountFeign;

    public Optional<List<AccountDto>> retrieveActivatedByConditionsOrNot(Optional<String> type, Optional<String> currency) {
        return Optional.of(accountFeign.retrieveActivatedByConditionsOrNot(type.orElse(null), currency.orElse(null)));
    }

    public AccountDto retrieveActivatedByName(String name) {
        return accountFeign.retrieveActivatedByName(name);
    }

    public AccountDto create(AccountDto accountDto) {
        return accountFeign.createOne(accountDto);
    }

    public void deleteOne(@NotBlank UUID id) {
        accountFeign.deleteOne(id);
    }
}
