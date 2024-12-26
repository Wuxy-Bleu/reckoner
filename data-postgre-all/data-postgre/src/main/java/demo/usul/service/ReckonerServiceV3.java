package demo.usul.service;

import cn.hutool.core.text.CharSequenceUtil;
import demo.usul.convert.LoanMapper;
import demo.usul.dto.LoanCreateDto;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.entity.ReckonerLoanUnionPage;
import demo.usul.entity.ReckonerUnionQuery;
import demo.usul.repository.AccountRepository;
import demo.usul.repository.LoanRepository;
import demo.usul.repository.ReckonerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static demo.usul.dto.LoanDto.LoanType.ADVANCED_CONSUMPTION;

@Slf4j
@Service
public class ReckonerServiceV3 {

    private final ReckonerRepository reckonerRepository;
    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final LoanService loanService;
    private final LoanMapper loanMapper;

    @Autowired
    public ReckonerServiceV3(ReckonerService reckonerService, ReckonerRepository reckonerRepository, LoanRepository loanRepository, AccountRepository accountRepository, AccountService accountService, LoanService loanService, LoanMapper loanMapper) {
        this.reckonerRepository = reckonerRepository;
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    // reckoner+loan双表分页查询
    public ReckonerLoanUnionPage getAllTransactions(Pageable pageable) {
        // 先交易时间倒序union offset+limit获取id list，再分别取两个表中fetch
        ReckonerUnionQuery unionQuery = loanRepository.findAllTransactionsPageableUnion2Table(pageable);
        List<UUID> rList = unionQuery.getPage().stream().filter(e -> "a".equals(e.getSource())).map(ReckonerUnionQuery.Union::getId).toList();
        List<UUID> lList = unionQuery.getPage().stream().filter(e -> "b".equals(e.getSource())).map(ReckonerUnionQuery.Union::getId).toList();
        List<ReckonerEntity> reckonerPage = reckonerRepository.findByIdInAndIsAliveTrueOrderByTransDateDesc(rList);
        List<LoanEntity> loanPage = loanRepository.findByIdIn(lList);

        // set page结果，caller不会拿到传入的page_size，而是page的实际size(<= pageSize)，不过它自己传的不需要我反给它
        ReckonerLoanUnionPage page = new ReckonerLoanUnionPage();
        page.setReckonerPage(reckonerPage);
        page.setLoanPage(loanPage);
        page.setPageNum(pageable.getPageNumber());
        page.setPageSize(rList.size() + lList.size());
        page.setReckonerPageNum(pageable.getPageNumber());
        page.setReckonerPageSize(rList.size());
        page.setLoanPageNum(pageable.getPageNumber());
        page.setLoanPageSize(lList.size());
        page.setTotal(unionQuery.getTotalSize());
        return page;
    }

    @Transactional
    public String deleteReckoner(UUID id) {
        int i = reckonerRepository.updateIsAliveByIsAliveTrueAndId(id);
        log.info("{} reckoner deleted from table", i);
        return MessageFormat.format("{0} reckoner deleted", i);
    }

    @Transactional
    public ReckonerEntity createAdvancedConsumption(ReckonerEntity entity) {
        // 先用后付 默认先使用交行卡作为支付账号
        AccountEntity fromAcctObj = accountRepository.findByIdAndIsActiveTrue(UUID.fromString("b4561812-8330-42c1-b8e8-06160548de08"));
        entity.setFromAcctObj(fromAcctObj);
        return reckonerRepository.save(entity);
    }

    // reckoner重新设定支付账号，并删除，
    @Transactional
    public LoanEntity endAdvancedConsumption(UUID reckonerId, UUID acctId, OffsetDateTime transDate) {
        ReckonerEntity advancedConsumption = reckonerRepository.findByIdAndIsAliveTrue(reckonerId);
        AccountEntity fromAcct = accountRepository.findByIdAndIsActiveTrue(acctId);
        advancedConsumption.setFromAcctObj(fromAcct);
        advancedConsumption.setIsAlive(false);
        if (CharSequenceUtil.equals(fromAcct.getTypeName(), "Credit Card")) {
            LoanCreateDto loanCreateDto = loanMapper.reckonerEntity2LoanCreateDto(advancedConsumption);
            loanCreateDto.setTransDate(transDate);
            loanCreateDto.setLoanType(ADVANCED_CONSUMPTION.getType());
            LoanEntity entity = loanService.create(loanCreateDto);
            Map<String, Object> json = new HashMap<>();
            json.put("loan", entity.getId());
            advancedConsumption.setCol0(json);
            reckonerRepository.persist(advancedConsumption);
            return entity;
        }
        return null;
    }

    public List<ReckonerEntity> advancedConsumptions() {
        return reckonerRepository.findByInOutAndIsAliveTrueOrderByTransDateDesc((short) 2);
    }

    public void assetAggre(UUID fromAcct) {
        List<LoanEntity> entities = loanService.getConditional(fromAcct);
    }
}
