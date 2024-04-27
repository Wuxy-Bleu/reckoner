package demo.usul.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static demo.usul.consta.Constants.ACCTS_CACHE_TTL_MS;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    public static final String ACCT_CACHE_NAME = "accountsActivated";
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CacheFeign cacheFeign;


    /**
     * todo 有个大问题就是如果filter search cache没查到，也会refresh一次cache，这是多余的步骤，得想办法改
     *
     * @param id get acct by id, if not exist, get by name cardType and currency
     * @return not gone be null, empty list
     */
    public List<AccountDto> getOrRefreshCache(String id, String name, String cardType, String currency) {
        Optional<List<AccountDto>> opt;
        boolean switcher = CharSequenceUtil.isNotBlank(id);
        if (switcher) {
            AccountDto hit = cacheFeign.getCachedAcctById(id);
            opt = ObjectUtil.isEmpty(hit) ? Optional.empty() : Optional.of(List.of(hit));
        } else {
            opt = Optional.ofNullable(cacheFeign.getCachedAccts(name, cardType, currency)).filter(CollUtil::isNotEmpty);
        }
        if (opt.isPresent()) {
            return opt.get();
        }
        refreshCache();
        return switcher ? List.of(cacheFeign.getCachedAcctById(id))
                : cacheFeign.getCachedAccts(name, cardType, currency);
    }

    /**
     * @param toUpdate records for update in db, 每条record must have id, only update balance
     * @return todo int, void, 重查一次db哪个好呢
     */
    public void updateBlc(List<AccountDto> toUpdate) {
        Map<UUID, BigDecimal> toUpdateMap = toUpdate.stream().collect(Collectors.toMap(AccountDto::getId, AccountDto::getBalance));
        accountRepository.updateBalBatch(toUpdateMap);
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

    // 理论上不要修改balance，要不然会让记录很乱
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

    /**
     * 如何delete的话，这个就不适用了，需要改进
     *
     * @return
     */
    public void refreshCache() {
        accountRepository.findByIsActive(true)
                .map(accountMapper::accountEntities2Dtos)
                .ifPresent(dtos -> cacheFeign.cacheAccounts(ACCTS_CACHE_TTL_MS, dtos));
    }
}


