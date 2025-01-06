package demo.usul.controller;

import demo.usul.controller.handler.CacheHandler;
import demo.usul.dto.AccountDto;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import java.util.Arrays;
import java.util.List;

@Deprecated
@Configuration
public class CacheRoute {

    private static final List<String> words = Arrays.asList(
            "the",
            "quick",
            "brown",
            "fox",
            "jumped",
            "over",
            "the",
            "lazy",
            "dog"
    );

    private final ValidationFilter validationFilter;

    public CacheRoute(ValidationFilter validationFilter) {
        this.validationFilter = validationFilter;
    }

    public static void main(String[] args) {
        Hooks.resetOnOperatorDebug();
        Flux<String> manyLetters = Flux
                .fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .distinct()
                .sort()
                .zipWith(Flux.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string));

        manyLetters.subscribe(System.out::println);
    }

    @Bean
    @Valid
    public RouterFunction<ServerResponse> route(CacheHandler cacheHandler) {
        return RouterFunctions.route()
                .POST("/v3/cache/{ms}", cacheHandler::cacheAccounts)
                .filter(validationFilter.validate(AccountDto.class))
                .GET("/v3/cache/accts", cacheHandler::getCachedAccts)
                .GET("/cache/accts/v3/{id}", cacheHandler::getCachedAcctById)
                .build();
    }
}
