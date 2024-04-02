package demo.usul.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;


@Configuration
public class ReactiveWebClientConfig {

    @Value("${redisson-server.url:localhost}")
    private String url;
    @Value("${redisson-server.port}")
    private int port;

    // call redisson service reactively
    @Bean
    public WebClient redissonWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofHours(2))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000000);
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://" + url + ":" + port)
                .build();
    }
}
