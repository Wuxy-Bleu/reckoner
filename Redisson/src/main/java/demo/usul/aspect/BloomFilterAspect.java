package demo.usul.aspect;

import demo.usul.annotation.BloomFilterReactive;
import demo.usul.exceptions.BloomFilterException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.reactivestreams.Publisher;
import org.redisson.api.RBloomFilterReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.options.PlainOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

@Slf4j
@Aspect
@Component
public class BloomFilterAspect {

    private final RedissonReactiveClient redissonReactiveClient;
    private final SpelExpressionParser parser;

    // bf key的前缀，最好是bf-{service name or something}-
    @Value("${bloom.suffix}")
    private String suffix;

    @Autowired
    public BloomFilterAspect(RedissonReactiveClient redissonReactiveClient) {
        this.redissonReactiveClient = redissonReactiveClient;
        this.parser = new SpelExpressionParser();
    }

    @Pointcut("@annotation(demo.usul.annotation.BloomFilterReactive)")
    public void bloomFilterAblePointCut() {}

    // todo 对aop做unit test需要做哪些啊，是否获取到切面, aspect是否注入....
    @Around("bloomFilterAblePointCut()")
    public Object cacheablePointcut(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取切面的注解
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        BloomFilterReactive anno = method.getAnnotation(BloomFilterReactive.class);

        // 这是要被缓存在redis中的记录，一定要放method param第一个，利用SpEL获取到主键, expire ms放第二个
        //todo 对于注解所在method的参数类型和返回类型的判定, 抛出异常，同时调用端（数据服务）捕获feign调用的异常，不能因为缓存服务的错误导致数据服务不可用
        // 或者如何抽象为范型
        Object[] args = joinPoint.getArgs();
        String keyName = suffix + anno.key();
        List<Object> ids = ((Collection<?>) args[0])
                .stream()
                .map(e -> parser.parseExpression(anno.idGetter()).getValue(e))
                .toList();

        Object proceed = joinPoint.proceed();
        // 切面的返回值一定得是Mono or Flux
        // todo 对这个reative flow做StepVerifier unit test
        if (proceed instanceof Mono || proceed instanceof Flux<?>) {
            // redisBloom client
            RBloomFilterReactive<Object> bloomFilter = redissonReactiveClient.getBloomFilter(PlainOptions.name(keyName));
            // todo 这个操作是没有事务的，如果两个请求同时对同一个key做这四个操作会怎么样？？？
            Mono<Boolean> del = bloomFilter
                    .delete()
                    .doOnNext(e ->
                            trueLogAndReturnEmptyElseEx(true, "delete " + keyName + "return " + e, null));
            Mono<Boolean> init = bloomFilter
                    .tryInit(anno.capacity(), anno.errRate())
                    .doOnNext(e ->
                            trueLogAndReturnEmptyElseEx(e, "create bf succeed", "create bf keyName failed, " + keyName + " already exists"));
            Mono<Long> add = bloomFilter
                    .add(ids)
                    .doOnNext(e ->
                            trueLogAndReturnEmptyElseEx(e > 0, "add " + e + " keys to " + keyName, "add ids to" + keyName + "failed, RedissonBloomFilter.add return " + e));
            Mono<Boolean> expire = bloomFilter
                    .expire(Duration.ofMillis((Long) args[1]))
                    .doOnNext(e -> trueLogAndReturnEmptyElseEx(e, "set expire with " + args[1] + "ms success", "fail to set expire for " + keyName));
            //noinspection ReactiveStreamsUnusedPublisher
            return Mono.from((Publisher<?>) proceed)
                    .then(del)
                    .then(init)
                    .then(add)
                    .then(expire)
                    .doOnError(e -> log.error("bf fail", e));
        }
        throw new BloomFilterException("only capable for reactive method");
    }

    private void trueLogAndReturnEmptyElseEx(boolean b, String trueMsg, String exMsg) {
        if (b) {
            log.info(trueMsg);
        } else {
            throw new BloomFilterException(exMsg);
        }
    }
}
