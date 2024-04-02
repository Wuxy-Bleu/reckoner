package demo.usul.service;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import demo.usul.dto.AccountDto;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.search.query.QueryOptions;
import org.redisson.api.search.query.SearchResult;
import org.redisson.codec.JacksonCodec;
import org.redisson.codec.JsonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static demo.usul.consta.Constants.ACCTS_CACHE_KEY;
import static demo.usul.consta.Constants.ACCTS_IDX;

@Service
public class CacheReactiveService {

    private final RedissonReactiveClient redissonReactiveClient;
    private final JsonCodec<List<AccountDto>> codec4Accts;

    @Autowired
    public CacheReactiveService(RedissonReactiveClient redissonReactiveClient) {
        this.redissonReactiveClient = redissonReactiveClient;
        codec4Accts = new JacksonCodec<>(new TypeReference<>() {});
    }

    public Mono<Void> cacheAccountsReactive(List<AccountDto> accounts, Long ms) {
        return redissonReactiveClient.getJsonBucket(ACCTS_CACHE_KEY, codec4Accts)
                .set(accounts, Duration.ofMillis(ms));
    }

    public Mono<List<AccountDto>> getCachedAccounts(Optional<String> name, Optional<String> type) {
        Mono<SearchResult> res = redissonReactiveClient.getSearch().search(ACCTS_IDX, "@name: name", QueryOptions.defaults());
//        res.subscribe(r -> r.getDocuments().forEach(e -> Log.info(e.toString())));
//        Mono.empty().subscribe();
        return redissonReactiveClient.getJsonBucket(ACCTS_CACHE_KEY, codec4Accts).get();
    }



}
