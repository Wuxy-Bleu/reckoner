package demo.usul.service;

import cn.hutool.core.collection.CollUtil;
import demo.usul.convert.AccountMapper;
import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.entity.AccountEntity;
import demo.usul.feign.CacheFeign;
import demo.usul.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

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

    public static final String ACCT_CACHE_NAME = "accountsActivated";
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CacheFeign cacheFeign;

    public List<AccountDto> retrieveActivatedCacheable(Optional<String> name, Optional<String> cardType, Optional<String> currency) {
        List<AccountDto> accts = cacheFeign.getCachedAccts(name, cardType, currency);
        if (CollUtil.isEmpty(accts)) {
            Optional<List<AccountEntity>> accountsActivated = accountRepository.findByIsActive(true);
            accts = accountsActivated.map(e -> {
                List<AccountDto> accountDtos = accountMapper.accountEntities2Dtos(e);
                cacheFeign.cacheAccounts(100000L, accountDtos);
                log.info("save cache {} with {}ms", "ACCTS:CACHE", 100000);
                return cacheFeign.getCachedAccts(Optional.empty(), cardType, currency);
            }).orElse(null);
        } else {
            log.info("cache {} hit", "ACCTS:CACHE");
        }
        return accts;
    }

    //todo batch需要改动，对于部分成功，部分不成功的情况要重写逻辑，然后不要用foreach
    @CacheEvict(cacheNames = ACCT_CACHE_NAME, allEntries = true)
    public List<AccountDto> createBatch(List<AccountDto> accountDtos) {
        return accountDtos.stream().map(this::create).toList();
    }

    // card type不存在时error handler, 存取之后，find不到error handler，
    // 优化下，save之后find在repos层来写
    @CacheEvict(cacheNames = ACCT_CACHE_NAME, allEntries = true)
    public AccountDto create(AccountDto accountDto) {
        AccountEntity toSave = accountMapper.accountDto2Entity(accountDto);
        accountRepository.saveWithAssociations(toSave);
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

    @CacheEvict(cacheNames = ACCT_CACHE_NAME, allEntries = true)
    public List<AccountEntity> update(List<AccountUpdateDto> accountUpdateDtos) {
        final Map<UUID, AccountUpdateDto> toUpdate = accountUpdateDtos.stream()
                .collect(Collectors.toMap(AccountUpdateDto::getId, Function.identity()));
        return accountRepository.updateBatch(toUpdate, toUpdate.keySet());
    }

    public List<AccountDto> updateAndReturnDtos(List<AccountUpdateDto> accountUpdateDtos) {
        return accountMapper.accountEntities2Dtos(this.update(accountUpdateDtos));
    }

    // cache evict 是啥时候evict啊，我如果在这个方法中去取缓存还能命中吗
    @CacheEvict(cacheNames = ACCT_CACHE_NAME, allEntries = true)
    public int softDelete(List<UUID> delIds) {
        return accountRepository.softDelete(delIds);
    }

    public void compareWithExisting(List<AccountUpdateDto> accountUpdateDtos) {

    }

}
