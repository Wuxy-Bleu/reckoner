package demo.usul.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.usul.dto.AccountDto;
import io.vavr.CheckedFunction1;
import org.redisson.api.RScript;
import org.redisson.api.RScriptReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.search.query.QueryOptions;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JacksonCodec;
import org.redisson.codec.JsonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static demo.usul.consta.Constants.ACCTS_CACHE_KEY;
import static demo.usul.consta.Constants.ACCTS_IDX;
import static demo.usul.scripts.LuaScript.LOOP_SET_JSON;

@Service
public class CacheReactiveService {

    private final ObjectMapper objectMapper;
    private final RedissonReactiveClient redissonReactiveClient;
    private final RScriptReactive script;

    private final TypeReference<AccountDto> trAcct = new TypeReference<>() {};
    private final TypeReference<List<AccountDto>> trAccts = new TypeReference<>() {};
    private final JsonCodec<List<AccountDto>> codec4Accts;
    private final JsonCodec<AccountDto> codec4Acct;

    @Autowired
    public CacheReactiveService(RedissonReactiveClient redissonReactiveClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.redissonReactiveClient = redissonReactiveClient;
        // 这里的codec不是用来给返回报文反序列化的吗，那么给请求报文序列化的是啥, 貌似也是codec??
        script = redissonReactiveClient.getScript(new StringCodec());
        codec4Acct = new JacksonCodec<>(objectMapper, trAcct);
        codec4Accts = new JacksonCodec<>(objectMapper, trAccts);
    }

    public Mono<AccountDto> getCachedAcctById(String id) {
        return redissonReactiveClient.getJsonBucket(ACCTS_CACHE_KEY + id, codec4Acct)
                .get();
    }

    public Mono<List<AccountDto>> getCachedAcctsByIds(List<String> ids) {
        // mget只能写script或者直接command调用
        return Mono.empty();
    }

    // validation, dtos must have id
    public Mono<Void> cacheAccountsReactive(List<AccountDto> accounts, Long ms) {
        List<Object> keys = accounts.stream().map(e -> (Object) (ACCTS_CACHE_KEY + e.getId().toString())).toList();

        Stream<Object> tmp = accounts.stream()
                .map(CheckedFunction1.liftTry(objectMapper::writeValueAsString))
                .map(t -> t.getOrElseThrow(e -> new RuntimeException(e)));
        Object[] args = Stream.concat(tmp, Stream.of(ms)).toArray();

        return script
                .scriptLoad(LOOP_SET_JSON)
                .flatMap(hash ->
                        script.evalSha(RScript.Mode.READ_WRITE, hash, RScript.ReturnType.STATUS, keys, args));
    }

    @SuppressWarnings("unchecked")
    public Mono<List<AccountDto>> getCachedAccounts(Optional<String> name, Optional<String> cardType, Optional<String> currency) {
        TypedJsonJacksonCodec typedJsonJacksonCodec = new TypedJsonJacksonCodec(trAcct, null, trAcct, objectMapper);
        return redissonReactiveClient
                .getSearch(typedJsonJacksonCodec)
                .search(ACCTS_IDX, queryAccts(name, cardType, currency), QueryOptions.defaults())
                .flatMap(search -> {
                    if (search.getTotal() > 0)
                        return Mono.just(
                                search.getDocuments().stream().map(doc -> (AccountDto) doc.getAttributes().get("$")).toList()
                        );
                    return Mono.empty();
                });
    }

    private String queryAccts(Optional<String> name, Optional<String> cardType, Optional<String> currency) {
        String nameParam = name.flatMap(s -> Optional.of("@name: " + s)).orElse("");
        String typeParam = cardType.flatMap(s -> Optional.of("@cardType: " + s)).orElse("");
        String currencyParam = currency.flatMap(s -> Optional.of("@currency: " + s)).orElse("");
        String query = nameParam + " " + typeParam + " " + currencyParam;
        return StringUtils.hasText(query) ? query : "*";
    }

}
