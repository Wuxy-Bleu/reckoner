package demo.usul.controller;

import demo.usul.dto.AccountDto;
import demo.usul.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/v2/accounts")
public class AccountAsyncController {

    private final AccountService accountService;

    @Autowired
    public AccountAsyncController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @Async
    public CompletableFuture<List<AccountDto>> retrieveActivatedByConditionsOrNot() {
//        return CompletableFuture.completedFuture(accountService.retrieveActivatedCacheable());
        return null;
    }
}
