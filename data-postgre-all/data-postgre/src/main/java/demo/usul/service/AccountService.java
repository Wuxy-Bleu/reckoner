package demo.usul.service;

import demo.usul.convert.AccountMapper;
import demo.usul.entity.AccountEntity;
import demo.usul.feign.dto.AccountDto;
import demo.usul.feign.dto.AccountUpdateDto;
import demo.usul.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;


    @Cacheable(cacheNames = "accountsActivated", sync = true, key = "#root.methodName")
    public List<AccountDto> retrieveActivatedCacheable() {
        Optional<List<AccountEntity>> accountsActivated = accountRepository.findByIsActive(true);
        return accountMapper.accountEntities2Dtos(accountsActivated.orElse(Collections.emptyList()));
    }

    @CacheEvict(cacheNames = "accountsActivated", allEntries = true)
    public List<AccountDto> createBatch(List<AccountDto> accountDtos) {
        List<AccountEntity> accountsInserted = accountRepository.saveAll(accountMapper.accountDtos2Entities(accountDtos));
        return accountMapper.accountEntities2Dtos(accountsInserted);
    }

    @CacheEvict(cacheNames = "accountsActivated", allEntries = true)
    public AccountDto create(AccountDto accountDto) {
        AccountEntity toSave = accountMapper.accountDto2Entity(accountDto);
        accountRepository.saveAssociations(toSave);
        Optional<AccountEntity> saved = accountRepository.findById(toSave.getId());
        return accountMapper.accountEntity2Dto(saved.orElse(null));
    }

    public AccountEntity retrieveActivatedByName(String name) {
        // self-invocation issues, 但是给这个方法添加缓存我觉得没必要，可以写一个无缓存的实现
        return accountRepository.findByNameIgnoreCase(name).orElse(null);
    }

    public void deleteById(UUID id) {
        accountRepository.deleteById(id);
    }

    public List<AccountEntity> update(List<AccountUpdateDto> accountUpdateDtos) {
        final Map<UUID, AccountUpdateDto> toUpdate = accountUpdateDtos.stream()
                .collect(Collectors.toMap(AccountUpdateDto::getId, Function.identity()));
        return accountRepository.updateBatch(toUpdate, toUpdate.keySet());
    }

    @Transactional
    public List<AccountDto> updateAndReturnDtos(List<AccountUpdateDto> accountUpdateDtos) {
        return accountMapper.accountEntities2Dtos(this.update(accountUpdateDtos));
    }

    public int softDelete(List<UUID> delIds) {
       return accountRepository.softDelete(delIds);
    }
}
