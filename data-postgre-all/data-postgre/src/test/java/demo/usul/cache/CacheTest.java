package demo.usul.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import demo.usul.dto.AccountUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.OxmSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CacheTest {

    private final ArrayList<AccountUpdateDto> dtos = new ArrayList<>(List.of(AccountUpdateDto.builder().name("xxxx").build()));
    private final CollectionType collectionType = new ObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, AccountUpdateDto.class);

    // redisSerializer的6种子类
    // 实际使用中 应该可以customize 各种的redisSerializer注册进去
    private final RedisSerializer<Object> jdkSerializer = new JdkSerializationRedisSerializer();  // 这是spring cache redis默认的serializer
    private final RedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(collectionType);
    private final RedisSerializer<Object> genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
    private final RedisSerializer<ArrayList> genericToStringSerializer = new GenericToStringSerializer<>(ArrayList.class, StandardCharsets.UTF_8);
    private final RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
    private final RedisSerializer<Object> oxmSerializer = new OxmSerializer();

    @Test
    void test_whichRedisSerializer_canSerializeList() {
        // 其中5个可以序列化 list<pojo>
        assertThat(jdkSerializer.canSerialize(dtos.getClass())).isTrue();
        assertThat(jackson2JsonRedisSerializer.canSerialize(dtos.getClass())).isTrue();
        assertThat(genericJackson2JsonRedisSerializer.canSerialize(dtos.getClass())).isTrue();
//        assertThat(genericToStringSerializer.canSerialize(dtos.getClass())).isTrue();
//        assertThat(oxmSerializer.canSerialize(dtos.getClass())).isTrue();
        assertThat(stringRedisSerializer.canSerialize(dtos.getClass())).isFalse();

        // 理论上序列化不会报错，无关是否准确
        assertThat(jdkSerializer.serialize(dtos)).isNotNull().isNotEmpty();
        // jackson的默认序列化方法，不知道跟jdk默认的序列化有啥区别  com.fasterxml.jackson.databind.ObjectMapper.writeValueAsBytes
        assertThat(jackson2JsonRedisSerializer.serialize(dtos)).isNotEmpty();
        assertThat(genericJackson2JsonRedisSerializer.serialize(dtos)).isNotEmpty();
        // 通过某个org.springframework.core.convert.converter.GenericConverter 将source object 转为string，再string.getBytes, 大多数情况下需要自定义converter, 而且感觉这个serializer不该如果使用
//        assertThat(genericToStringSerializer.serialize(dtos)).isNotEmpty();
        // spring object xml mapping, 不适用
//        assertThat(oxmSerializer.serialize(dtos)).isNotEmpty();
    }
}
