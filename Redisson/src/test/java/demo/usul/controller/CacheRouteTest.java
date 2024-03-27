package demo.usul.controller;

import demo.usul.controller.handler.CacheHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("local")
class CacheRouteTest {

    @Autowired
    private RouterFunction<ServerResponse> routerFunction;
    @MockBean
    private CacheHandler cacheHandler;
    @Autowired
    private ApplicationContext applicationContext;
    private WebTestClient webTestClient;

    @BeforeEach
    void createWebTestClient() {
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void contextLoads() {
        assertThat(routerFunction).isNotNull();
        CacheHandler bean = applicationContext.getBean(CacheHandler.class);
        assertThat(bean).isSameAs(cacheHandler);
    }

    @Test
    void testIfCacheHandlerBeInvoked() {
        when(cacheHandler.cacheAccounts(any())).thenReturn(ServerResponse.ok().build());
        webTestClient.post()
                .uri("/v3/cache/100")
                .exchange()
                .expectStatus().isOk();
        verify(cacheHandler, Mockito.times(1)).cacheAccounts(any());
    }


}
