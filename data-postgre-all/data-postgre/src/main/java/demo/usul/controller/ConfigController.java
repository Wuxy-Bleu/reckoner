package demo.usul.controller;

import demo.usul.properties.ConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("v1/configserver")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigProperties configProperties;

    @GetMapping
    public String greeting() {
        return configProperties.getGreeting();
    }

}
