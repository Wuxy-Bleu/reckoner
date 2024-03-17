package demo.usul.service;

import com.fasterxml.jackson.core.type.TypeReference;
import demo.usul.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RJsonBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class CacheService {

    private final RedissonClient redissonClient;

    @Autowired
    public CacheService(RedissonClient redissonClient) {this.redissonClient = redissonClient;}

    public void cacheAccounts(List<AccountDto> accounts, Long ms) {
        RJsonBucket<List<AccountDto>> jsonBucket = redissonClient.getJsonBucket("what for??",
                new JacksonCodec<>(new TypeReference<List<AccountDto>>() {}));
        jsonBucket.set(accounts, Duration.ofMillis(ms));
    }

}
