package demo.usul.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CuratorClientConfig {

    // from config server
    @Value("${zookeeper.url}")
    private String zooUrl;

    @Bean
    public CuratorFramework curatorZookeeperClient() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zooUrl)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("snow")
                .connectionTimeoutMs(5000000)
                .sessionTimeoutMs(5000000)
                .build();
        curatorFramework.getConnectionStateListenable().addListener((client, newState) -> log.info("State changed to: " + newState));
        curatorFramework.start();
        return curatorFramework;
    }
}
