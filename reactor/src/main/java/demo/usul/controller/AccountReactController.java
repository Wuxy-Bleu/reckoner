package demo.usul.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/v1/react/accounts")
public class AccountReactController {

    @GetMapping
    public Mono<String> getSOmething() {
        return Mono.just("test");
    }
}
