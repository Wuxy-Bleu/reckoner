package demo.usul.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import demo.usul.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.redisson.api.RJsonBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class CacheRouteV2Test {

    @Autowired
    private RouterFunction<ServerResponse> routerFunction;
    @MockBean
    private RedissonClient redissonClient;

    @Test
    void testIfRedissonSetCorrectValue() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunction).build();
        RJsonBucket<List<AccountDto>> jsonBucket = mock(redissonClient)
                .getJsonBucket("key",
                        new JacksonCodec<>(new TypeReference<List<AccountDto>>() {}));
//        Mockito.doCallRealMethod().when(jsonBucket).set();
//        when(jsonBucket.set(anyList(), Duration.ofMillis(Mockito.anyLong())))


    }
}
