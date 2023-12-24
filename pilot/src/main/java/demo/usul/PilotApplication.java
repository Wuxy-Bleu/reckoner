package demo.usul;

import demo.usul.anno.EnableLocale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableLocale
public class PilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PilotApplication.class, args);
    }
}
