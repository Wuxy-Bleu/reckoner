package demo.usul.controller;

import demo.usul.feign.SnowFlakeFeign;
import demo.usul.feign.TryFeign;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@EqualsAndHashCode
@RestController
@RequestMapping("/v1/test")
public class TestController {

    private final SnowFlakeFeign snowFlakeFeign;
    private final TryFeign tryFeign;

    @Autowired
    public TestController(SnowFlakeFeign snowFlakeFeign, TryFeign tryFeign) {
        this.snowFlakeFeign = snowFlakeFeign;
        this.tryFeign = tryFeign;
    }

    @GetMapping
    public Long test() {
        return snowFlakeFeign.generateSnowFlakeId();
    }

    @GetMapping
    public Mono<String> tead() {
        return tryFeign.getSOmething();
    }
}
