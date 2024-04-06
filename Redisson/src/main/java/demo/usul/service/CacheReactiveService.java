package demo.usul.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.usul.dto.AccountDto;
import demo.usul.scripts.LuaScript;
import io.vavr.CheckedFunction0;
import org.redisson.api.RScript;
import org.redisson.api.RScriptReactive;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.search.query.QueryOptions;
import org.redisson.codec.JacksonCodec;
import org.redisson.codec.JsonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static demo.usul.consta.Constants.ACCTS_CACHE_KEY;
import static demo.usul.consta.Constants.ACCTS_IDX;

@Service
public class CacheReactiveService {

    private final RedissonReactiveClient redissonReactiveClient;
    private final RedissonClient redissonClient;

    private final TypeReference<List<AccountDto>> typeReference;
    private final JsonCodec<List<AccountDto>> codec4Accts;
    private final RScriptReactive script;
    private final ObjectMapper objectMapper;

    @Autowired
    public CacheReactiveService(RedissonReactiveClient redissonReactiveClient, RedissonClient redissonClient) {
        this.redissonReactiveClient = redissonReactiveClient;
        this.redissonClient = redissonClient;
        codec4Accts = new JacksonCodec<>(new TypeReference<>() {});
        typeReference = new TypeReference<>() {};
        script = redissonReactiveClient.getScript();
        objectMapper = new ObjectMapper();
    }

    // validation, dtos must have id
    public Mono<Void> cacheAccountsReactive(List<AccountDto> accounts, Long ms) {
//        RBatch batch = redissonClient.createBatch();
//        RJsonBucketAsync<List<AccountDto>> jsonBucket = batch.getJsonBucket(ACCTS_CACHE_KEY, codec4Accts);
//        RFuture<List<AccountDto>> voidRFuture = jsonBucket.getAndSetAsync(accounts, Duration.ofMillis(ms));
//        voidRFuture.get();
//        RJsonBucket<List<AccountDto>> jsonBucket1 = redissonClient.getJsonBucket(ACCTS_CACHE_KEY, codec4Accts);

//        RScript script1 = redissonClient.getScript(new StringCodec());
        List<Object> keys = accounts.stream().map(e -> (Object) (ACCTS_CACHE_KEY + ":" + e.getId().toString())).toList();
        Object[] args = accounts.stream().map(e -> CheckedFunction0.lift(() -> objectMapper.writeValueAsString(e))).toArray();

        return script
                .scriptLoad(LuaScript.LOOP_SET_JSON)
                .flatMap(hash ->
                        script.evalSha(RScript.Mode.READ_WRITE, hash, RScript.ReturnType.STATUS, keys, args));

    }

    @SuppressWarnings("unchecked")
    public Mono<List<AccountDto>> getCachedAccounts(Optional<String> name, Optional<String> type, Optional<String> currency) {
        TypedJsonJacksonCodec typedJsonJacksonCodec = new TypedJsonJacksonCodec(typeReference, null, typeReference);
        return redissonReactiveClient.getSearch(typedJsonJacksonCodec)
                .search(ACCTS_IDX, queryAccts(name, type, currency), QueryOptions.defaults())
                .flatMap(search -> {
                    if (search.getTotal() > 0)
                        return Mono.just((List<AccountDto>) search.getDocuments().get(0).getAttributes().get("$"));
                    return Mono.empty();
                });
    }

    private String queryAccts(Optional<String> name, Optional<String> type, Optional<String> currency) {
        String nameParam = name.flatMap(s -> Optional.of("@name: " + s)).orElse("");
        String typeParam = type.flatMap(s -> Optional.of("@type: " + s)).orElse("");
        String currencyParam = currency.flatMap(s -> Optional.of("@currency: " + s)).orElse("");
        String query = nameParam + " " + typeParam + " " + currencyParam;
        return StringUtils.hasText(query) ? query : "*";
    }

}
