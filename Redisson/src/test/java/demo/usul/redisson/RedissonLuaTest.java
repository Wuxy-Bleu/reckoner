package demo.usul.redisson;

public class RedissonLuaTest {

    public final static String IF_INDEX_EXIST = """
            local indexName = KEYS[1]
            local exists = redis.call('ft.info', KEYS[1])
            """;
}
