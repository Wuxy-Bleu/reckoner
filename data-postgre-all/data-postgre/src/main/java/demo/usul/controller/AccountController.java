package demo.usul.controller;

import demo.usul.convert.AccountMapper;
import demo.usul.feign.dto.AccountDto;
import demo.usul.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    private final List<Converter> converters;

    // 其实这里复杂的查询过滤最好使用 自定义的requestObj
    // 或者criteria 更通用
    @GetMapping
    public List<AccountDto> retrieveActivatedByConditionsOrNot(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String currency) {
        List<AccountDto> accountDtos = accountService.retrieveActivatedCacheable();
        Stream<AccountDto> cachedStream = accountDtos.stream();
        if (null != type) {
            if (null != currency)
                return cachedStream.filter(e -> type.equalsIgnoreCase(e.getCardType()) && currency.equalsIgnoreCase(e.getCurrency())).toList();
            else
                return cachedStream.filter(e -> type.equalsIgnoreCase(e.getCardType())).toList();
        } else {
            if (null != currency)
                return cachedStream
                        .filter(e -> currency.equalsIgnoreCase(e.getCurrency())).toList();
            else
                return cachedStream.toList();
        }

    }

    // path variable for unique column查询
    @GetMapping("/{name}")
    public AccountDto retrieveActivatedByName(@PathVariable String name) {
        // feign 数据传递，如果查不到是返回null好，还是抛出异常呢
        // 说实话好丑陋啊
        return this.retrieveActivatedByConditionsOrNot(null, null)
                .stream().filter(e -> name.equals(e.getName())).findAny().orElse(null);
    }

    // 如果部分插入成功，部分失败，那么是否能准确的插入成功的部分
    @PostMapping("/batch")
    public List<AccountDto> createBatch(@RequestBody List<AccountDto> accountDtos) {
        return accountService.createBatch(accountDtos);
    }

    @PostMapping()
    public AccountDto createOne(@Valid @RequestBody AccountDto accountDto) {
        return accountService.create(accountDto);
    }

    @DeleteMapping()
    public Integer delete(@RequestBody List<String> delIds) {
        log.info(delIds.get(0));
        return 1;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        accountService.deleteById(id);
    }

}
