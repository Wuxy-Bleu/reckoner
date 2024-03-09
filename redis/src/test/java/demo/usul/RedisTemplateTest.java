package demo.usul;

import demo.usul.dto.AccountUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "local")
class RedisTemplateTest {

    final AccountUpdateDto accountUpdateDto = AccountUpdateDto.builder().name("random name").build();
    final List<AccountUpdateDto> dtos = List.of(accountUpdateDto);

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testIfRedisTemplate4Object_workFine() {
        redisTemplate.opsForValue().set("abd", dtos, 1000 * 60 * 10, MILLISECONDS);
        redisTemplate.opsForValue().set("yuy", accountUpdateDto, 1000 * 60 * 10, MILLISECONDS);

        assertThat(redisTemplate.getValueSerializer()).isInstanceOf(Jackson2JsonRedisSerializer.class);
        assertThat(redisTemplate.getKeySerializer()).isInstanceOf(StringRedisSerializer.class);
//        assertThat((List<AccountUpdateDto>) redisTemplate.opsForValue().get("abd"))
//                .singleElement().isInstanceOf(AccountUpdateDto.class);

        Object o = redisTemplate.opsForValue().get("abd");
        Object o1 = redisTemplate.opsForValue().get("yuy");
    }

}
