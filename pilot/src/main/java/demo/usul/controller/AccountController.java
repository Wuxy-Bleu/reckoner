package demo.usul.controller;

import demo.usul.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Value("${spring.application.name}")
    private String name;

    @GetMapping
    public String test() {

        return accountService.xxx();

    }

    @GetMapping("/gg")
    public String xx() {
        return name;
    }
}
