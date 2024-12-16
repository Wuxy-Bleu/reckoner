package demo.usul.controller;

import demo.usul.convert.ReckonerMapper;
import demo.usul.dto.ReckonerDto;
import demo.usul.entity.ReckonerEntity;
import demo.usul.service.AccountService;
import demo.usul.service.ReckonerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/reckoner")
public class ReckonerController {

    private final ReckonerService reckonerService;
    private final AccountService accountService;
    private final ReckonerMapper reckonerMapper;

    @GetMapping("/count/from/{acct}")
    public Long countByFromAcct(@PathVariable UUID acct) {
        return reckonerService.countDistinctByFromAcctAllIgnoreCase(acct);
    }

    @GetMapping("/count/to/{acct}")
    public Long countByToAcct(@PathVariable UUID acct) {
        return reckonerService.countDistinctByToAcctAllIgnoreCase(acct);
    }

    @GetMapping("/from/{name}")
    public List<ReckonerDto> retrieveByFromAcctName(@PathVariable String name,
                                                    @RequestParam(required = false) Optional<List<String>> tags,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(defaultValue = "0") Integer pageNum) {
        if (tags.isEmpty()) {
            return reckonerMapper.reckonerEntities2Dtos(reckonerService.retrieveByFromAcctName(name, PageRequest.of(pageNum, pageSize)));
        } else {
            return reckonerMapper.reckonerEntities2Dtos(reckonerService.retrieveByFromAcctNameAndTags(name, tags.get()));
        }
    }

    @GetMapping("/tags")
    public List<ReckonerDto> retrieveByTags(@RequestParam List<String> tags) {
        return reckonerMapper.reckonerEntities2Dtos(reckonerService.retrieveByTags(tags));
    }


    @GetMapping("/to/{name}")
    public Page<ReckonerDto> retrieveByToAcctName(@PathVariable String name,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "0") Integer pageNum) {
        Page<ReckonerEntity> page = reckonerService.retrieveByToAcctName(name, PageRequest.of(pageNum, pageSize));
        return new PageImpl<>(reckonerMapper.reckonerEntities2Dtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    @GetMapping("")
    public Page<ReckonerDto> retrieveAll(@RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(defaultValue = "0") Integer pageNum) {
        return reckonerService.retrieveAll(pageSize, pageNum);
    }

    @PostMapping("")
    public ReckonerDto createOne(@RequestBody @Valid ReckonerDto reckoner, @RequestParam(required = false) Optional<Boolean> trigger) {
        ReckonerEntity entity = reckonerService.createOne(reckoner, trigger.orElse(true));
        accountService.refreshCache();
        return reckonerService.retrieveById(entity.getId());
    }

    @GetMapping("/latest/created_date")
    public ReckonerDto latestCreated() {
        return reckonerService.latestCreated();
    }

    @GetMapping("latest/transaction_date")
    public ReckonerDto latestTransaction() {
        return reckonerService.latestTransaction();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        reckonerService.deleteById(id);
    }

}
