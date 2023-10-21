package demo.usul;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HelloApplication {

    @Value("${test}")
    private String test;

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    @GetMapping("/xxxx")
    public String test() {
        return test;
    }
}
