package demo.usul.controller;

import demo.usul.service.HelloService;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EqualsAndHashCode
@RestController
@RequestMapping("/v1/test")
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {this.helloService = helloService;}

    @GetMapping
    public Long test() {
        helloService.doSomething();
        return 1L;
    }

}
