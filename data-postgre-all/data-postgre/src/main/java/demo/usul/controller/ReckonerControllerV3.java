package demo.usul.controller;

import demo.usul.convert.LoanMapper;
import demo.usul.dto.TransactionPage;
import demo.usul.service.LoanService;
import demo.usul.service.ReckonerService;
import demo.usul.service.ReckonerServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("reckoner/v3")
public class ReckonerControllerV3 {

    private final ReckonerServiceV3 reckonerServiceV3;
    private final LoanMapper loanMapper;
    private final LoanService loanService;

    @Autowired
    public ReckonerControllerV3(ReckonerService reckonerService, LoanService loanService, ReckonerServiceV3 reckonerServiceV3, LoanMapper loanMapper, LoanService loanService1) {
        this.reckonerServiceV3 = reckonerServiceV3;
        this.loanMapper = loanMapper;
        this.loanService = loanService;
    }

    @GetMapping
    public TransactionPage getAllTransactions(@RequestParam int pageNum, @RequestParam int pageSize) {
        return loanMapper.mergeToTransactionPage(reckonerServiceV3.getAllTransactions(pageNum, pageSize));
    }

    @DeleteMapping
    public void deleteTransaction(@RequestParam("id") UUID id, @RequestParam("is_loan") Boolean isLoan) {
        if (Boolean.TRUE.equals(isLoan))
            loanService.deleteLoan(id);
        else
            reckonerServiceV3.deleteReckoner(id);
    }
}
