package demo.usul;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableWebFlux
@Aspect
public class RedissonApplication {

    public static void main(String[] args) {
        Hooks.resetOnOperatorDebug();
        SpringApplication.run(RedissonApplication.class, args);
    }
}
