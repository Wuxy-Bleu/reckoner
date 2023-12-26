package demo.usul.controller;

import demo.usul.entity.AccountModifyRecord;
import demo.usul.service.AccountModifyRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account-update-record")
public class AccountModifyRecordController {

    private final AccountModifyRecordService accountModifyRecordService;

    @PostMapping("")
    public void createAccountModifyRecord(@RequestBody AccountModifyRecord accountModifyRecord) {
        accountModifyRecordService.createAccountModifyRecord(accountModifyRecord);
    }

}
