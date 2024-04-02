package demo.usul.cmd_runner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

@TestExecutionListeners(BlockHoundIntegration.class)
@SpringBootTest
class RedisIdxCmdRunnerTest {

    @Test
    void testSpringInit() {
//        Mono.delay(Duration.ofSeconds(1))
//                .doOnNext(it -> {
//                    try {
//                        Thread.sleep(10);
//                    }
//                    catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .block();
    }
}