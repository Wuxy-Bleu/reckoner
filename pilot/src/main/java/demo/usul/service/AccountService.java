package demo.usul.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import demo.usul.converter.AccountMapper;
import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountModifyRecordDto;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.feign.AccountFeign;
import demo.usul.feign.AccountModifyRecordFeign;
import demo.usul.feign.CacheFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AccountService {

    private final AccountFeign accountFeign;
    private final AccountModifyRecordFeign accountModifyRecordFeign;
    private final AccountMapper accountMapper;
    private final CsvMapper csvMapper;
    private final CacheFeign cacheFeign;

    @Autowired
    public AccountService(AccountFeign accountFeign, AccountModifyRecordFeign accountModifyRecordFeign, AccountMapper accountMapper, CsvMapper csvMapper, CacheFeign cacheFeign) {
        this.accountFeign = accountFeign;
        this.accountModifyRecordFeign = accountModifyRecordFeign;
        this.accountMapper = accountMapper;
        this.csvMapper = csvMapper;
        this.cacheFeign = cacheFeign;
    }

    // call data-postgre service, using redis cache
    // 改成call cache，不过要想办法解决并发问题
    public List<AccountDto> retrieveActivatedByConditionsOrNot(Optional<String> cardType, Optional<String> currency) {
        return cacheFeign.getCachedAccts(null, cardType.orElse(null), currency.orElse(null));
    }

    public String retrieveAsCsv(Optional<String> type, Optional<String> currency) throws JsonProcessingException {
        List<AccountDto> accountDtos = retrieveActivatedByConditionsOrNot(type, currency);

        CsvSchema headers = csvMapper.schemaFor(AccountDto.class).withHeader();
        return csvMapper.writer(headers.withUseHeader(true))
                .writeValueAsString(accountDtos);
    }

    public List<AccountModifyRecordDto> retrieveModifyHistory(String uuid) {
        return accountModifyRecordFeign.retrieveModifyRecord(uuid);
    }

    public void createModifyHistory(List<AccountModifyRecordDto> accountModifyRecordDtos) {
        accountModifyRecordFeign.createModifyRecordBatch(accountModifyRecordDtos);
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
        createModifyHistory(accountMapper.accountUpdateDtos2ModifyRecordDtos(accountUpdateDtos));
        return accountFeign.update(accountUpdateDtos);
    }

    public void compareBeforeUpdate(List<AccountUpdateDto> accountUpdateDtos) {
    }
}
