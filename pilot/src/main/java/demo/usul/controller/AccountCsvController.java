package demo.usul.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import demo.usul.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/csv/accounts")
public class AccountCsvController {

    private final AccountService accountService;

    @GetMapping
    public String retrieveActivatedByConditionsOrNot(
            @RequestParam(required = false) Optional<String> type,
            @RequestParam(required = false) Optional<String> currency) throws JsonProcessingException {
        return accountService.retrieveAsCsv(type, currency);
    }

}
