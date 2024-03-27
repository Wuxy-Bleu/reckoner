package demo.usul.service;

import com.fasterxml.jackson.core.type.TypeReference;
import demo.usul.dto.AccountDto;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.JacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class CacheServiceV2 {

    private final RedissonReactiveClient redissonReactiveClient;

    @Autowired
    public CacheServiceV2(RedissonReactiveClient redissonReactiveClient) {
        this.redissonReactiveClient = redissonReactiveClient;
    }

    public Mono<Void> cacheAccountsReactive(List<AccountDto> accounts, Long ms) {
        return redissonReactiveClient.getJsonBucket("key-reactive",
                        new JacksonCodec<>(new TypeReference<>() {}))
                .set(accounts, Duration.ofMillis(ms));
    }


}
