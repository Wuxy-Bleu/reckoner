package demo.usul.controller;

import demo.usul.convert.LoanMapper;
import demo.usul.convert.ReckonerMapper;
import demo.usul.dto.ReckonerCreate;
import demo.usul.dto.TransactionPage;
import demo.usul.dto.TransactionQueryCriteria;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.Loan_AccountAggre;
import demo.usul.entity.ReckonerEntity;
import demo.usul.service.LoanService;
import demo.usul.service.ReckonerService;
import demo.usul.service.ReckonerServiceV3;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static demo.usul.dto.ReckonerDto.InOutEnum.ADVANCED_CONSUMPTION;
import static org.springframework.data.domain.PageRequest.of;

@Slf4j
@RestController
@RequestMapping("reckoner/v3")
public class ReckonerControllerV3 {

    private final ReckonerServiceV3 reckonerServiceV3;
    private final LoanMapper loanMapper;
    private final LoanService loanService;
    private final ReckonerMapper reckonerMapper;
    private final ReckonerService reckonerService;

    @Autowired
    public ReckonerControllerV3(LoanService loanService, ReckonerServiceV3 reckonerServiceV3, LoanMapper loanMapper, LoanService loanService1, ReckonerMapper reckonerMapper, ReckonerService reckonerService) {
        this.reckonerServiceV3 = reckonerServiceV3;
        this.loanMapper = loanMapper;
        this.loanService = loanService;
        this.reckonerMapper = reckonerMapper;
        this.reckonerService = reckonerService;
    }

    // 分页获取所有的交易记录 loan+reckoner (not deleted)
    @GetMapping
    public TransactionPage getAllTransactions(@RequestParam int pageNum, @RequestParam int pageSize) {
        return loanMapper.mergeToTransactionPage(reckonerServiceV3.getAllTransactions(of(pageNum, pageSize)));
    }

    @PostMapping("/criteria")
    public TransactionPage getAllTransactionsCriteria(@RequestBody TransactionQueryCriteria criteria) {
        return loanMapper.mergeToTransactionPage(reckonerServiceV3.getAllTransactionsCriteria(criteria));
    }

    // 删除一条交易记录 reckoner or loan(loan会删除对应的schedules, 都是更改status-> deleted)
    @DeleteMapping
    public String deleteTransaction(@RequestParam("id") UUID id, @RequestParam("is_loan") Boolean isLoan) {
        if (Boolean.TRUE.equals(isLoan))
            return loanService.deleteLoan(id);
        else
            return reckonerServiceV3.deleteReckoner(id);
    }

    // 新增一条先用后付记录
    @PostMapping("/advanced_consumption")
    public ReckonerEntity createAdvancedConsumption(@RequestBody @Valid ReckonerCreate create) {
        if (create.getInOut() == ADVANCED_CONSUMPTION.getInOut().shortValue()) {
            return reckonerServiceV3.createAdvancedConsumption(reckonerMapper.reckonerCreate2ReckonerEntity(create));
        }
        return null;
    }

    // 关闭一条先用后付记录，并新增一条关联的交易记录
    @PostMapping("/advanced_consumption/end")
    public LoanEntity endAdvancedConsumption(@RequestParam("reckoner_id") UUID reckonerId,
                                             @RequestParam("acct_id") UUID acctId,
                                             @RequestParam("trans_date") OffsetDateTime transDate) {
        return reckonerServiceV3.endAdvancedConsumption(reckonerId, acctId, transDate);
    }

    @GetMapping("/advanced_consumption")
    public List<ReckonerEntity> advancedConsumptions() {
        return reckonerServiceV3.advancedConsumptions();
    }

    //资产状况
    @GetMapping("/asset")
    public List<Loan_AccountAggre> assetAggre() {
        return reckonerServiceV3.assetAggre();
    }
}
