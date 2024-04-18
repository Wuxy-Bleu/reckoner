package demo.usul.controller;

import demo.usul.controller.handler.CacheHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CacheRoute {

    @Bean
    public RouterFunction<ServerResponse> route(CacheHandler cacheHandler) {
        return RouterFunctions.route()
                .POST("/v3/cache/{ms}", cacheHandler::cacheAccounts)
                .GET("/v3/cache/accts", cacheHandler::getCachedAccts)
                .GET("/cache/accts/v3/{id}", cacheHandler::getCachedAcctById)
                .build();
    }

}
