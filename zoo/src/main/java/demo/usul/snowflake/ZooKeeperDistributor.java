package demo.usul.snowflake;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static demo.usul.snowflake.SnowflakeID.SEPARATOR;
import static demo.usul.snowflake.SnowflakeID.SEQUENTIAL_PREFIX;
import static demo.usul.snowflake.util.SnowflakeUtil.sequentialSuffix;
import static org.apache.zookeeper.CreateMode.EPHEMERAL_SEQUENTIAL;

@Slf4j
@Component
public class ZooKeeperDistributor {

    public static final String COUNTER_PATH = "/counter";
    public static final String COUNTER_LOCK_PATH = "/counter-lock";
    public static final String MACHINE_ID_CACHE_KEY = "machine-id";
    public static final String SEQUENCE_ID_CACHE_KEY = "sequence-id";

    private final CuratorFramework curatorFramework;
    private final Cache<Object, Object> cache;

    private final InterProcessMutex lock;
    private final SharedCount sharedCount;

    @Autowired
    public ZooKeeperDistributor(CuratorFramework curatorFramework, Cache<Object, Object> cache) {
        this.curatorFramework = curatorFramework;
        this.cache = cache;
        this.lock = new InterProcessMutex(this.curatorFramework, COUNTER_LOCK_PATH);
        this.sharedCount = new SharedCount(this.curatorFramework, COUNTER_PATH, 0);
    }

    public Integer nextSequenceId() {
        Integer sequenceId = (Integer) cache.getIfPresent(SEQUENCE_ID_CACHE_KEY);
        sequenceId += 1;
        cache.put(SEQUENCE_ID_CACHE_KEY, sequenceId);
        return sequenceId;
    }

    public Integer getMachineId() throws Exception {

        Integer machineId = (Integer) cache.getIfPresent(MACHINE_ID_CACHE_KEY);
        if (null == machineId) {
            machineId = getMachineIdNative();
            cache.put(MACHINE_ID_CACHE_KEY, machineId);
        }
        return machineId;
    }

    public Integer getMachineIdNative() throws Exception {
        // distributed lock
        lock.acquire();
        sharedCount.start();
        int count = sharedCount.getCount();
        count++;
        sharedCount.setCount(count);
        sharedCount.close();
        lock.release();
        return count;
    }

    public long distribute() throws Exception {
        String path = curatorFramework.create().withMode(EPHEMERAL_SEQUENTIAL).forPath(SEPARATOR + SEQUENTIAL_PREFIX);
        log.info("path --> {}", path);
        return sequentialSuffix(path);
    }


}
