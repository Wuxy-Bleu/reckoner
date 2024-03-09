package demo.usul.controller;

import demo.usul.snowflake.SnowflakeID;
import demo.usul.snowflake.ZooKeeperDistributor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
//@EqualsAndHashCode
@RequiredArgsConstructor
@RequestMapping("/v1/snowflake")
public class SnowFlakeController {

    private final ZooKeeperDistributor zooKeeperDistributor;

    @GetMapping
    public Long generateSnowFlakeId() throws Exception {
        Integer machineId = zooKeeperDistributor.getMachineId();
        return SnowflakeID.generateDefault(machineId, zooKeeperDistributor.nextSequenceId());
    }

    @GetMapping("/friendly")
    public String generateFriendly() throws Exception {
        long machineId = zooKeeperDistributor.distribute();
        return SnowflakeID.generateFriendlyDefault(machineId, 5);
    }

    @GetMapping("/length/{id}")
    public Integer length(@PathVariable Long id) {
        return String.valueOf(id).length();
    }

    @GetMapping("/timespan")
    public Long span() {
        Instant start = Instant.ofEpochMilli(0);
        return 0L;
    }

    @GetMapping("/for-test")
    public int test() {
        log.info("hashcode --> {}", this.hashCode());
        log.info("thread --> {}", Thread.currentThread().getName());
        return this.hashCode();
    }
}
