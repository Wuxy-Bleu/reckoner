package demo.usul.redisson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RJsonBucket;
import org.redisson.api.RScript;
import org.redisson.api.RSearch;
import org.redisson.api.RedissonClient;
import org.redisson.api.search.index.FieldIndex;
import org.redisson.api.search.index.IndexOptions;
import org.redisson.api.search.index.IndexType;
import org.redisson.codec.JacksonCodec;
import org.redisson.config.Config;

import java.util.List;

import static demo.usul.redisson.RedissonLuaTest.IF_INDEX_EXIST;
import static org.assertj.core.api.Assertions.assertThatCode;

class RedissonSearchTest {

    RedissonClient client;
    RScript script;
    String shaExists;
    RSearch search;
    ObjectMapper objectMapper;
    String jsonString = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";

    public RedissonSearchTest() {
        Config config = new Config();
        config.useSingleServer().setDatabase(0).setAddress("redis://localhost:5377").setConnectTimeout(10000);
        client = Redisson.create(config);
        script = client.getScript();
        shaExists = script.scriptLoad(IF_INDEX_EXIST);
        search = client.getSearch();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateIndex() {
        search.createIndex("create_new_index",
                IndexOptions.defaults().on(IndexType.JSON).prefix("jsonDoc:"),
                FieldIndex.text("name"));

        assertThatCode(() -> script.evalSha(RScript.Mode.READ_ONLY, shaExists, RScript.ReturnType.MULTI, List.of("create_new_index"))).doesNotThrowAnyException();
    }

    @Test
    void testAddJsonDocument2Index() {
        RJsonBucket<String> jsonBucket = client.getJsonBucket("jsonDoc:demo", new JacksonCodec<>(new TypeReference<>() {}));
        // 没法将jsonString这样子插入到redis中，或许得研究研究序列化器来实现，但是redisson最好还是将pojos作为json插入到redis中
        jsonBucket.set(jsonString);
    }
}
