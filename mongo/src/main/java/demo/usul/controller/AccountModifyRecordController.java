package demo.usul.controller;

import demo.usul.dto.AccountModifyRecordDto;
import demo.usul.mapping.AccountModifyRecordMapper;
import demo.usul.service.AcctModifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account-update-record")
public class AccountModifyRecordController {

    private final AcctModifyService acctModifyService;
    private final AccountModifyRecordMapper accountModifyRecordMapper;

    @PostMapping("")
    public void createModifyRecord(@RequestBody AccountModifyRecordDto accountModifyRecordDto) {
        acctModifyService.createAccountModifyRecord(
                accountModifyRecordMapper.accountModifyRecordDto2Entity(accountModifyRecordDto));
    }

    @GetMapping("/{uuid}")
    public List<AccountModifyRecordDto> retrieveModifyRecord(@PathVariable String uuid) {
        return accountModifyRecordMapper.accountModifyRecordEntities2Dtos(
                acctModifyService.retrieveModifyRecord(uuid));
    }

    @PostMapping("/batch")
    public void createModifyRecordBatch(@RequestBody List<AccountModifyRecordDto> accountModifyRecordDtos) {
        acctModifyService.createAccountModifyRecords(
                accountModifyRecordMapper.accountModifyRecordDtos2Entities(accountModifyRecordDtos));
    }


}
