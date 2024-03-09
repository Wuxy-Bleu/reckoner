package demo.usul.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static demo.usul.snowflake.ZooKeeperDistributor.SEQUENCE_ID_CACHE_KEY;

@Configuration
public class CaffeineConfig {

    @Bean
    public Cache<Object, Object> cache() {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                // cache no expire
                .expireAfterWrite(Long.MAX_VALUE, TimeUnit.MINUTES)
                .maximumSize(10)
                .build();
        // snowflake id 需要的自增的sequence id, initial -> 0
        cache.put(SEQUENCE_ID_CACHE_KEY, 0);
        return cache;
    }
}
