package demo.usul.service;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.usul.dto.AccountDto;
import io.vavr.CheckedFunction1;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.redisson.api.RScriptReactive;
import org.redisson.api.RSearchReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.search.query.QueryOptions;
import org.redisson.api.search.query.SearchResult;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JacksonCodec;
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


@Slf4j
@Service
public class CacheReactiveService {

    private final ObjectMapper objectMapper;
    private final RedissonReactiveClient redissonReactiveClient;

    private final RScriptReactive script;

    private final TypeReference<AccountDto> trAcct = new TypeReference<>() {};
    private final TypeReference<List<AccountDto>> trAccts = new TypeReference<>() {};
    private final JacksonCodec<List<AccountDto>> codec4Accts;
    private final JacksonCodec<AccountDto> codec4Acct;
    private final TypedJsonJacksonCodec typedJsonJacksonCodec;
    private final RSearchReactive search;


    @Autowired
    public CacheReactiveService(RedissonReactiveClient redissonReactiveClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.redissonReactiveClient = redissonReactiveClient;
        // 这里的codec不是用来给返回报文反序列化的吗，那么给请求报文序列化的是啥, 貌似也是codec??
        script = redissonReactiveClient.getScript(new StringCodec());
        codec4Acct = new JacksonCodec<>(objectMapper, trAcct);
        codec4Accts = new JacksonCodec<>(objectMapper, trAccts);
        typedJsonJacksonCodec = new TypedJsonJacksonCodec(trAcct, null, trAcct, objectMapper);
        search = redissonReactiveClient.getSearch(typedJsonJacksonCodec);
    }

    public Mono<AccountDto> getCachedAcctById(String id) {
        return redissonReactiveClient
                .getJsonBucket(ACCTS_CACHE_KEY + id, codec4Acct)
                .get()
                .map(e -> (AccountDto) e);
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

    public Mono<List<AccountDto>> getCachedAccounts(Optional<String> name, Optional<String> cardType, Optional<String> currency) {

        return Mono.just(new Pageable(0, 10, 0))
                .flatMap(ele -> queryPageable(name, cardType, currency, ele))
                .expand(ele ->
                        CollUtil.isEmpty(ele.getSearchResult().getDocuments()) ?
                                Mono.empty() :
                                queryPageable(name, cardType, currency,
                                        new Pageable(ele.getPage().getPageNum() + 1,
                                                ele.getPage().getPageSize(),
                                                ele.getPage().getOffset() + ele.getPage().getPageSize())))
                .collectList()
                .map(ele -> ele
                        .stream()
                        .flatMap(e ->
                                e.getSearchResult().getDocuments().stream().map(doc -> (AccountDto) doc.getAttributes().get("$"))).toList());
    }

    private Mono<IntermediateObj> queryPageable(Optional<String> name, Optional<String> cardType, Optional<String> currency, Pageable pageable) {
        return search.search(
                        ACCTS_IDX, queryAccts(name, cardType, currency), QueryOptions.defaults().limit(pageable.getOffset(), pageable.getPageSize()))
                .map(res -> new IntermediateObj(pageable, res));
    }

    private String queryAccts(Optional<String> name, Optional<String> cardType, Optional<String> currency) {
        String nameParam = name.flatMap(s -> Optional.of("@name: " + s)).orElse("");
        String typeParam = cardType.flatMap(s -> Optional.of("@cardType: " + s)).orElse("");
        String currencyParam = currency.flatMap(s -> Optional.of("@currency: " + s)).orElse("");
        String query = nameParam + " " + typeParam + " " + currencyParam;
        return StringUtils.hasText(query) ? query : "*";
    }

    @AllArgsConstructor
    @Getter
    static
    class Pageable {

        private int pageNum;
        private int pageSize;
        private int offset;
    }

    @Getter
    @AllArgsConstructor
    static
    class IntermediateObj {

        Pageable page;
        SearchResult searchResult;
    }

}
