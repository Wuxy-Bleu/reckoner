package demo.usul.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {


    @PostMapping("/log")
    public String index(@RequestBody String body) throws ClassNotFoundException, NoSuchMethodException {

        System.out.println(body);
        return "Greetings from Spring Boot!";
    }


}
