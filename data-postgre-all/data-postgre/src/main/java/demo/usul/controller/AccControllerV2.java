package demo.usul.controller;

import demo.usul.dto.AccountDto;
import demo.usul.service.AccountService;
import demo.usul.service.RcknSvcV2;
import demo.usul.service.ReckonerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/v2/acc")
public class AccControllerV2 {

    private final AccountService accountService;
    private final RcknSvcV2 rcknSvcV2;

    @Autowired
    public AccControllerV2(AccountService accountService, ReckonerService reckonerService, RcknSvcV2 rcknSvcV2) {
        this.accountService = accountService;
        this.rcknSvcV2 = rcknSvcV2;
    }

    @DeleteMapping("/reset")
    public void reset() {
        accountService.reset();
        rcknSvcV2.reset();
    }

    @PutMapping()
    public AccountDto updateBalance(@RequestParam("name") String name,
                                    @RequestParam("balance") BigDecimal balance) {
//        accountService.updateAndReturnDtos();
        return null;
    }
}
