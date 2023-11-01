package demo.usul.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.usul.properties.ConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lab/v1/")
@RequiredArgsConstructor
@Slf4j
public class LabController {

    private final ConfigProperties configProperties;

    private final ObjectMapper objectMapper;

    @GetMapping
    public String greeting() {
        return configProperties.getGreeting();
    }

    @GetMapping("logs")
    public String logs() throws JsonProcessingException {
        log.info("xxxx");
//        ((LabController) AopContext.currentProxy()).jackson();
        return "xxxx";
    }

    @GetMapping("jackson")
    public String jackson() throws JsonProcessingException {
//        boolean b = objectMapper.canSerialize(OffsetDateTime.class);
//        String s = objectMapper.writeValueAsString(AccountDto.builder().createdAt(OffsetDateTime.now()).build());
//        return s;
        return null;
    }
}
