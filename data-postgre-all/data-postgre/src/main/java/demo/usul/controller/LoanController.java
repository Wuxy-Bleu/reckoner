package demo.usul.controller;

import demo.usul.dto.LoanCreateDto;
import demo.usul.entity.LoanEntity;
import demo.usul.service.LoanService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/loan")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {this.loanService = loanService;}

    @PostMapping()
    public Object createLoan(@RequestBody @Valid LoanCreateDto loanCreateDto) {
        return loanService.create(loanCreateDto);
    }

    @GetMapping
    public List<LoanEntity> getAll() {
        return loanService.getAll();
    }
}
