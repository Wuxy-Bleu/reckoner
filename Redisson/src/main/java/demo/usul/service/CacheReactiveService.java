package demo.usul.service;

import com.fasterxml.jackson.core.type.TypeReference;
import demo.usul.dto.AccountDto;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.search.query.QueryOptions;
import org.redisson.codec.JacksonCodec;
import org.redisson.codec.JsonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    TypeReference<List<AccountDto>> typeReference;

    @Autowired
    public CacheReactiveService(RedissonReactiveClient redissonReactiveClient) {
        this.redissonReactiveClient = redissonReactiveClient;
        codec4Accts = new JacksonCodec<>(new TypeReference<>() {});
        typeReference = new TypeReference<>() {};
    }

    public Mono<Void> cacheAccountsReactive(List<AccountDto> accounts, Long ms) {
        return redissonReactiveClient.getJsonBucket(ACCTS_CACHE_KEY, codec4Accts)
                .set(accounts, Duration.ofMillis(ms));
    }

    @SuppressWarnings("unchecked")
    public Mono<List<AccountDto>> getCachedAccounts(Optional<String> name, Optional<String> type) {

        TypedJsonJacksonCodec typedJsonJacksonCodec = new TypedJsonJacksonCodec(typeReference, null, typeReference);
        return redissonReactiveClient.getSearch(typedJsonJacksonCodec)
                .search(ACCTS_IDX, queryAccts(name, type), QueryOptions.defaults())
                .flatMap(search -> {
                    if (search.getTotal() > 0)
                        return Mono.just((List<AccountDto>) search.getDocuments().get(0).getAttributes().get("$"));
                    return Mono.empty();
                });
    }

    private String queryAccts(Optional<String> name, Optional<String> type) {
        String nameParam = name.flatMap(s -> Optional.of("@name: " + s)).orElse("");
        String typeParam = type.flatMap(s -> Optional.of("@type: " + s)).orElse("");
        String query = nameParam + " " + typeParam;
        return StringUtils.hasText(query) ? query : "*";
    }

}
