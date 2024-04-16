package demo.usul.controller.handler;

import demo.usul.dto.AccountDto;
import demo.usul.service.CacheReactiveService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CacheHandler {

    private final CacheReactiveService cacheService;

    public CacheHandler(CacheReactiveService cacheService) {
        this.cacheService = cacheService;
    }

    public Mono<ServerResponse> cacheAccounts(ServerRequest req) {
        return req.bodyToFlux(AccountDto.class)
                .collectList()
                .flatMap(e -> cacheService.cacheAccountsReactive(e,
                        Long.valueOf(req.pathVariable("ms"))))
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> getCachedAccts(ServerRequest req) {
        return cacheService.getCachedAccounts(req.queryParam("name"), req.queryParam("cardType"), req.queryParam("currency"))
                .flatMap(accts ->
                        ServerResponse.ok().body(BodyInserters.fromValue(accts)));
    }
}
