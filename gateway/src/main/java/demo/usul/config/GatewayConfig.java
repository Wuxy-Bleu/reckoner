package demo.usul.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("accounts", r -> r.path("/accounts")
                        .filters(f ->
                                f.circuitBreaker(c -> c.setName("myCircuitBreaker")
                                                .setFallbackUri("forward:/inCaseOfFailureUseThis"))
                                        .rewritePath("/accounts(?<segment>.*)", "/v1/accounts$\\{segment}")
                                        )
                        .uri("http://localhost:8080")
                )
                .build();
    }
}
