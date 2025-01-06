package demo.usul.service;

import demo.usul.beans.CachedAcctsDto;
import demo.usul.convert.AccountMapper;
import demo.usul.dto.AccountCriteria;
import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.entity.AccountEntity;
import demo.usul.feign.CacheFeign;
import demo.usul.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.equalsIgnoreCase;
import static cn.hutool.core.util.ObjectUtil.isNotNull;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static demo.usul.consta.Constants.ACCTS_CACHE_TTL_MS;
import static demo.usul.dto.AccountCriteria.EMPTY_CRITERIA;

@Slf4j
@Service
public class AccountService {

    public static final String ACCT_CACHE_NAME = "accountsActivated";
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CacheFeign cacheFeign;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, CacheFeign cacheFeign) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.cacheFeign = cacheFeign;
    }

    // 不包括deleted
    public List<AccountDto> getAll() {
        return getOrRefreshCache(EMPTY_CRITERIA);
    }

    public List<AccountDto> getOrRefreshCache(AccountCriteria criteria) {
        // 传入null值应该返回所有data, 这里finalCriteria是为了在lambda中使用该变量，
        // 如果isnull ? criteria = EMPTY_CRITERIA就不是effectively final的了, An effectively final variable is one whose value is not reassigned after initialization
        AccountCriteria finalCriteria = isNull(criteria) ? EMPTY_CRITERIA : criteria;
        // try fetch from redis 布隆过滤器保证了是否真的cache miss or just Capacity Miss
        CachedAcctsDto cached;
        boolean byId = isNotNull(finalCriteria.getId());
        if (byId)
            cached = cacheFeign.getById(finalCriteria.getId());
        else
            // cache api不使用criteria是因为 cache set中查询不应该有太多条件，不该有太复杂的查询，且参数全空并不影响执行效率
            cached = cacheFeign.getCachedAccounts(finalCriteria.getName(), finalCriteria.getCardType(), finalCriteria.getCurrency());
        if (cached.isHit()) return cached.getCached();
        // if cache miss, fetch all from db and redis刷新缓存
        List<AccountDto> datas = accountMapper.accountEntities2Dtos(accountRepository.findByIsActiveTrueOrderByBalanceDesc());
        cacheFeign.cacheAccounts(datas, ACCTS_CACHE_TTL_MS);
        if (byId)
            return datas.stream().filter(e -> e.getId().equals(finalCriteria.getId())).toList();
        else
            return datas.stream().filter(p(finalCriteria)).toList();
    }

    private Predicate<AccountDto> p(AccountCriteria criteria) {
        if (isNull(criteria.getName()) && isNull(criteria.getCardType()) && isNull(criteria.getCurrency()))
            return e -> true;
        else
            return e -> equalsIgnoreCase(criteria.getName(), e.getName())
                        && equalsIgnoreCase(criteria.getCardType(), e.getCardType())
                        && equalsIgnoreCase(criteria.getCurrency(), e.getCurrency());
    }

    /**
     * @param toUpdate records for update in db, 每条record must have id, only update balance
     */
    public int updateBlc(List<AccountDto> toUpdate) {
        Map<UUID, BigDecimal> toUpdateMap = toUpdate.stream().collect(Collectors.toMap(AccountDto::getId, AccountDto::getBalance));
        return accountRepository.updateBalBatch(toUpdateMap);
    }

    //todo batch需要改动，对于部分成功，部分不成功的情况要重写逻辑，然后不要用foreach
    public List<AccountDto> createBatch(List<AccountDto> accountDtos) {
        return accountDtos.stream().map(this::create).toList();
    }

    // card type不存在时error handler, 存取之后，find不到error handler，
// 优化下，save之后find在repos层来写
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

    public List<AccountEntity> update(List<AccountUpdateDto> accountUpdateDtos) {
        final Map<UUID, AccountUpdateDto> toUpdate = accountUpdateDtos.stream()
                .collect(Collectors.toMap(AccountUpdateDto::getId, Function.identity()));
        return accountRepository.updateBatch(toUpdate, toUpdate.keySet());
    }

    public List<AccountDto> updateAndReturnDtos(List<AccountUpdateDto> accountUpdateDtos) {
        return accountMapper.accountEntities2Dtos(this.update(accountUpdateDtos));
    }

    public int softDelete(List<UUID> delIds) {
        return accountRepository.softDelete(delIds);
    }
}


