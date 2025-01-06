package demo.usul.service;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.usul.annotation.BloomFilterReactive;
import demo.usul.beans.CachedAcctsDto;
import demo.usul.dto.AccountDto;
import io.vavr.CheckedFunction1;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
import java.util.UUID;
import java.util.stream.Stream;

import static demo.usul.consta.Constants.ACCTS_CACHE_KEY;
import static demo.usul.consta.Constants.ACCTS_IDX;
import static demo.usul.scripts.LuaScript.LOOP_SET_JSON;

/**
 * 所有的缓存查询的返回类型都是一个额外的bean class, 其中有个字段表明是否hit
 * 是否hit会使用redis bloom来判定
 */
@Slf4j
@Service
public class CacheReactiveService {

    private final ObjectMapper objectMapper;
    private final RedissonReactiveClient redissonReactiveClient;

    @SuppressWarnings("FieldCanBeLocal")
    private final TypeReference<AccountDto> trAcct = new TypeReference<>() {};
    private final JacksonCodec<AccountDto> codec4Acct;
    @SuppressWarnings("FieldCanBeLocal")
    private final TypedJsonJacksonCodec typedJsonJacksonCodec;

    private final RScriptReactive script;
    private final RSearchReactive search;


    @Autowired
    public CacheReactiveService(RedissonReactiveClient redissonReactiveClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.redissonReactiveClient = redissonReactiveClient;
        // codec for不同的redis client
        codec4Acct = new JacksonCodec<>(objectMapper, trAcct);
        typedJsonJacksonCodec = new TypedJsonJacksonCodec(trAcct, null, trAcct, objectMapper);
        // script reactive client && redisSearch reactive client
        script = redissonReactiveClient.getScript(new StringCodec());
        search = redissonReactiveClient.getSearch(typedJsonJacksonCodec);
    }

    public Mono<CachedAcctsDto> getCachedAcctById(UUID id) {
        return redissonReactiveClient
                .getJsonBucket(ACCTS_CACHE_KEY + id.toString(), codec4Acct)
                .get()
                .map(e -> {
                    if (null != e)
                        return new CachedAcctsDto().addAcctDto((AccountDto) e);
                    else
                        return new CachedAcctsDto().notHit();
                });
    }

    public Mono<List<AccountDto>> getCachedAcctsByIds(List<String> ids) {
        // mget只能写script或者直接command调用
        return Mono.empty();
    }

    // 缓存账号list, 并设置expire
    // 流程 -> obj list转json str, lua脚本for loop JSON.SET, 一个call搞定，事务安全
    // todo 可以尝试对cache的key设置expire listener来重新缓存数据，但是不直接call data服务，而是利用mq来做异步的数据交换，具体来需要设计
    @BloomFilterReactive(key = "demoXx")
    public Mono<Object> cacheAccountsReactive(List<AccountDto> accounts, Long ms) {
        List<Object> keys = accounts.stream().map(e -> (Object) (ACCTS_CACHE_KEY + e.getId().toString())).toList();

        Stream<Object> tmp = accounts.stream()
                // lambda中catch and throw异常不好写，所以使用了vavr的工具
                .map(CheckedFunction1.liftTry(objectMapper::writeValueAsString))
                // 这里不能写成RuntimeException::new是因为会产生歧义,
                // getOrElseThrow有两个override, runtime exception有两个构造器都各自符合一个override
                // todo 这里直接new runtime exception不好，最好自定义
                .map(t -> t.getOrElseThrow(e -> new RuntimeException(e)));
        // 账号list转array, expire time放到arr末尾
        Object[] args = Stream.concat(tmp, Stream.of(ms)).toArray();

        return script
                .scriptLoad(LOOP_SET_JSON)
                .flatMap(hash ->
                        script
                                .evalSha(RScript.Mode.READ_WRITE, hash, RScript.ReturnType.STATUS, keys, args)
                                .doOnNext(str -> log.info("lua script response -> {}", str))
                );
    }

    // optional参数是因为 之前使用router function, serverRequest.queryParam return optional, 就先不改了，影响不大
    public Mono<CachedAcctsDto> getCachedAccounts(Optional<String> name, Optional<String> cardType, Optional<String> currency) {
        // todo 分页还很简陋，未完成状态
        return Mono.just(new Pageable(0, 10, 0))
                // query redisSearch返回IntermediateObj是因为没办法在reactive流中将多次分页请求的SearchResult(redisson search原本的返回obj type)整理为一个list
                .flatMap(ele -> queryPageable(name, cardType, currency, ele))
                // expand真的是我花了好久才找到的reactor的方法，好用
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
                                e.getSearchResult().getDocuments().stream().map(doc -> (AccountDto) doc.getAttributes().get("$"))).toList())
                .map(dtos -> {
                    if (CollUtil.isNotEmpty(dtos))
                        return new CachedAcctsDto().addAcctDtos(dtos);
                    else
                        return new CachedAcctsDto().notHit();
                });
    }

    // redisson redisSearch reactive client call search
    // 一定要设置offset和count，因为ft.search默认只返回10个我记得，
    private Mono<IntermediateObj> queryPageable(Optional<String> name, Optional<String> cardType, Optional<String> currency, Pageable pageable) {
        return search.search(
                        ACCTS_IDX, queryAccts(name, cardType, currency), QueryOptions.defaults().limit(pageable.getOffset(), pageable.getPageSize()))
                .map(res -> new IntermediateObj(pageable, res));
    }

    // redisSearch 查询命令string 构建
    private String queryAccts(Optional<String> name, Optional<String> cardType, Optional<String> currency) {
        String nameParam = name.flatMap(s -> Optional.of("@name: " + s)).orElse("");
        String typeParam = cardType.flatMap(s -> Optional.of("@cardType: " + s)).orElse("");
        String currencyParam = currency.flatMap(s -> Optional.of("@currency: " + s)).orElse("");
        String query = nameParam + " " + typeParam + " " + currencyParam;
        return StringUtils.hasText(query) ? query : "*";
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Pageable {

        private int pageNum;
        private int pageSize;
        private int offset;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class IntermediateObj {

        Pageable page;
        SearchResult searchResult;
    }

}
