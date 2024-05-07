package demo.usul.controller;

import demo.usul.dto.AcctBlcCalculateDto;
import demo.usul.service.AcctModifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/accts_balance_adjust/v1")
public class AcctBlcCalController {

    private final AcctModifyService acctModifyService;

    public AcctBlcCalController(AcctModifyService acctModifyService) {this.acctModifyService = acctModifyService;}

    @PostMapping
    public void saveOne(@RequestBody AcctBlcCalculateDto dto) {
        acctModifyService.saveOne(dto);
    }

    @PostMapping("/all")
    public void saveAll(@RequestBody List<AcctBlcCalculateDto> dtos) {
        acctModifyService.saveAll(dtos);
    }

    @GetMapping
    public List<AcctBlcCalculateDto> retrieve() {
        return acctModifyService.retrieve();
    }

    @GetMapping("/rckn/{id}")
    public AcctBlcCalculateDto retrieveOne(@PathVariable UUID id) {
        return acctModifyService.retrieveOne(id);
    }

    @PutMapping
    public AcctBlcCalculateDto update(@RequestBody AcctBlcCalculateDto dto) {
        return acctModifyService.updateOne(dto);
    }
}
