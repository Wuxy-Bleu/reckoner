package demo.usul.controller;

import demo.usul.convert.LoanMapper;
import demo.usul.dto.LoanCreateDto;
import demo.usul.dto.LoanDto;
import demo.usul.service.LoanService;
import demo.usul.service.ReckonerServiceV3;
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
    private final LoanMapper loanMapper;
    private final ReckonerServiceV3 reckonerServiceV3;

    @Autowired
    public LoanController(LoanService loanService, LoanMapper loanMapper, ReckonerServiceV3 reckonerServiceV3) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
        this.reckonerServiceV3 = reckonerServiceV3;
    }

    @PostMapping()
    public Object createLoan(@RequestBody @Valid LoanCreateDto loanCreateDto) {
        return loanService.create(loanCreateDto);
    }

    @GetMapping
    public List<LoanDto> getAll() {
        return loanMapper.loanEntities2Dtos(loanService.getAll());
    }
}
