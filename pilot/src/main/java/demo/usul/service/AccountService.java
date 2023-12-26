package demo.usul.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.feign.AccountFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private static final CsvMapper csvMapper = new CsvMapper();
    private final AccountFeign accountFeign;

    static {
        csvMapper.findAndRegisterModules();
    }

    public List<AccountDto> retrieveActivatedByConditionsOrNot(
            Optional<String> type, Optional<String> currency) {
        return Optional.of(
                        accountFeign.retrieveActivatedByConditionsOrNot(
                                type.orElse(null),
                                currency.orElse(null)))
                .orElse(Collections.emptyList());
    }

    public String retrieveAsCsv(Optional<String> type, Optional<String> currency) throws JsonProcessingException {
        List<AccountDto> accountDtos = retrieveActivatedByConditionsOrNot(type, currency);
        CsvSchema headers = csvMapper.schemaFor(AccountDto.class);
        return csvMapper.writer(
                        headers.withUseHeader(true))
                .writeValueAsString(accountDtos);
    }

    public AccountDto retrieveActivatedByName(String name) {
        return accountFeign.retrieveActivatedByName(name);
    }

    public AccountDto create(AccountDto accountDto) {
        return accountFeign.createOne(accountDto);
    }

    public Integer softDelete(List<UUID> delIds) {
        return accountFeign.delete(delIds);
    }

    public List<AccountDto> updateAndReturnDtos(List<AccountUpdateDto> accountUpdateDtos) {
        return accountFeign.update(accountUpdateDtos);
    }
}
