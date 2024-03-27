package demo.usul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class RedissonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedissonApplication.class, args);
    }
}
