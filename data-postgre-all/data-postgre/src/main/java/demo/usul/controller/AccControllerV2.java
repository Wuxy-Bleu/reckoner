package demo.usul.controller;

import demo.usul.service.AccountService;
import demo.usul.service.RcknSvcV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v2/acc")
public class AccControllerV2 {

    private final AccountService accountService;
    private final RcknSvcV2 rcknSvcV2;

    @Autowired
    public AccControllerV2(AccountService accountService, RcknSvcV2 rcknSvcV2) {
        this.accountService = accountService;
        this.rcknSvcV2 = rcknSvcV2;
    }
}
