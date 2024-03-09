package demo.usul.controller;

import demo.usul.dto.AccountUpdateDto;
import demo.usul.repository.AccountUpdateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/v1/cache")
public class CacheController {

    public static final String ACCOUNT_CACHE_PREFIX = "accounts:";
    private final RedisTemplate<Object, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final AccountUpdateRepository accountUpdateRepository;

    @Autowired
    public CacheController(RedisTemplate<Object, Object> redisTemplate, StringRedisTemplate stringRedisTemplate, AccountUpdateRepository accountUpdateRepository) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.accountUpdateRepository = accountUpdateRepository;
    }

    @PostMapping("/native")
    public void toCache(List<AccountUpdateDto> cacheable, Long ms) {
        redisTemplate.opsForValue().set("cache", cacheable, ms, TimeUnit.MILLISECONDS);
    }

    @PostMapping("/split-kv")
    public void toCacheKeyWithId(List<AccountUpdateDto> cacheable, Long ms) {
        ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
        cacheable.forEach(c -> {
            String key = ACCOUNT_CACHE_PREFIX + c.getId().toString();
            valueOperations.set(key, c);
            redisTemplate.expire(key, ms, TimeUnit.MILLISECONDS);
        });
    }

    @PostMapping("/hash")
    public void toCache2Hash(List<AccountUpdateDto> cacheable, Long ms) {
    }

    @GetMapping("/act")
    public void saveAct() {
        AccountUpdateDto sjakdjfalskjf = accountUpdateRepository.save(AccountUpdateDto.builder().name("sjakdjfalskjf").id(UUID.randomUUID()).balance(new BigDecimal(10)).build());
    }
}
