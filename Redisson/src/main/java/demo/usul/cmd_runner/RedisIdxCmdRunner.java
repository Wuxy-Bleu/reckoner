package demo.usul.cmd_runner;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.search.index.FieldIndex;
import org.redisson.api.search.index.IndexOptions;
import org.redisson.api.search.index.IndexType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static demo.usul.consta.Constants.ACCTS_CACHE_KEY;
import static demo.usul.consta.Constants.ACCTS_IDX;

@Slf4j
@Component
public class RedisIdxCmdRunner implements CommandLineRunner {

    private final RedissonReactiveClient redissonReactiveClient;

    @Autowired
    public RedisIdxCmdRunner(RedissonReactiveClient redissonReactiveClient) {this.redissonReactiveClient = redissonReactiveClient;}

    @Override
    public void run(String... args) {
        redissonReactiveClient.getSearch()
                .createIndex(ACCTS_IDX,
                        IndexOptions.defaults().prefix(ACCTS_CACHE_KEY).on(IndexType.JSON),
                        FieldIndex.text("name"), FieldIndex.text("type"))
                .onErrorResume(ex -> {
                    log.warn("maybe index already exist", ex);
                    return Mono.empty();
                })
                .subscribe();
    }
}
