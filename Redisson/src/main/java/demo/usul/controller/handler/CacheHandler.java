package demo.usul.controller.handler;

import demo.usul.dto.AccountDto;
import demo.usul.service.CacheServiceV2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CacheHandler {

    private final CacheServiceV2 cacheService;

    public CacheHandler(CacheServiceV2 cacheService) {
        this.cacheService = cacheService;
    }

    public Mono<ServerResponse> cacheAccounts(ServerRequest req) {
        return req.bodyToFlux(AccountDto.class)
                .collectList()
                .flatMap(e -> cacheService.cacheAccountsReactive(e,
                        Long.valueOf(req.pathVariable("ms"))))
                .then(ServerResponse.ok().build());
    }
}
