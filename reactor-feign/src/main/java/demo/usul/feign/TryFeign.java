package demo.usul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@FeignClient(name = "reactor-client")
public interface TryFeign {

    @GetMapping
    Mono<String> getSOmething();

}
