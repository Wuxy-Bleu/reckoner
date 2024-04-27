package demo.usul.controller;

import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.service.AccountService;
import demo.usul.service.AcctAggregationSvc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AcctAggregationSvc acctAggregationSvc;

    // 其实这里复杂的查询过滤最好使用 自定义的requestObj 或者criteria 更通用
    @GetMapping
    public List<AccountDto> retrieveActivatedByConditionsOrNot(
            @RequestParam(required = false) String cardType,
            @RequestParam(required = false) String currency) {
        return accountService.getOrRefreshCache(null, null, cardType, currency);
    }

    // path variable for unique column查询
    @GetMapping("/{name}")
    public AccountDto retrieveActivatedByName(@PathVariable String name) {
        // feign 数据传递，如果查不到是返回null好，还是抛出异常呢
        return accountService.getOrRefreshCache(null, name, null, null).get(0);
    }

    // 如果部分插入成功，部分失败，那么是否能准确的插入成功的部分
    @PostMapping("/batch")
    public List<AccountDto> createBatch(@RequestBody List<AccountDto> accountDtos) {
        return accountService.createBatch(accountDtos);
    }

    // todo 对于valid fail的error handler，暂时懒得管error了
    // todo 还有对于currency的valid, 然后name已存在的valid...
    @PostMapping()
    public AccountDto createOne(@Valid @RequestBody AccountDto accountDto) {
        return accountService.create(accountDto);
    }

    @DeleteMapping()
    public Integer delete(@RequestBody List<UUID> delIds) {
        return accountService.softDelete(delIds);
    }

    @DeleteMapping("/{id}")
    public Integer deleteById(@PathVariable UUID id) {
        return accountService.softDelete(List.of(id));
    }

    @PutMapping()
    public List<AccountDto> update(@RequestBody List<AccountUpdateDto> accountUpdateDtos) {
        return accountService.updateAndReturnDtos(accountUpdateDtos);
    }

    @GetMapping("/assets")
    public Map<String, String> allMyMoney() {
        return acctAggregationSvc.allMyMoney();
    }
}
