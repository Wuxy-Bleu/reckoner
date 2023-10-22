package demo.usul.service;

import demo.usul.convert.AccountMapper;
import demo.usul.dto.AccountDto;
import demo.usul.entity.AccountEntity;
import demo.usul.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    @Cacheable(cacheNames = "accountsActivated", sync = true, key = "#root.methodName")
    public List<AccountDto> retrieveActivatedCacheable() {
        Optional<List<AccountEntity>> accountsActivated = accountRepository.findByIsActive(true);
        if (accountsActivated.isPresent()) {
            List<AccountEntity> accounts = accountsActivated.get();
            return accountMapper.accountEntities2Dtos(accounts);
        } else {
            return Collections.emptyList();
        }
    }

    @CacheEvict(cacheNames = "accountsActivated", allEntries = true)
    public List<AccountDto> createBatch(List<AccountDto> accountDtos) {
        List<AccountEntity> accountsInserted = accountRepository.saveAll(accountMapper.accountDtos2Entities(accountDtos));
        return accountMapper.accountEntities2Dtos(accountsInserted);
    }

    @CacheEvict(cacheNames = "accountsActivated", allEntries = true)
    public AccountDto create(AccountDto accountDto) {
        return accountMapper.accountEntity2Dto(
                accountRepository.save(
                        accountMapper.accountDto2Entity(accountDto)));
    }

    public AccountDto retrieveActivatedByName(String name) {
        // self-invocation issues, 但是给这个方法添加缓存我觉得没必要，可以写一个无缓存的实现
        return null;
    }
}
