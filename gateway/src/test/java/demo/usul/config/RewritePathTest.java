package demo.usul.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

class RewritePathTest {

    @Test
    void customRouteLocator() {
        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
        FluxExchangeResult<String> stringFluxExchangeResult = webTestClient.get()
                .uri("/v1/reckoner")
                .exchange()
                .returnResult(String.class);

        Flux<String> responseBody = stringFluxExchangeResult.getResponseBody();
        responseBody.log();
    }
}