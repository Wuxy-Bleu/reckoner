package demo.usul.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import demo.usul.convert.LoanMapper;
import demo.usul.dto.LoanCreateDto;
import demo.usul.dto.TransactionQueryCriteria;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.Loan_AccountAggre;
import demo.usul.entity.Loan_AccountAggre.Loan_Account_YearMonthAggre;
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

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static demo.usul.dto.LoanDto.LoanType.ADVANCED_CONSUMPTION;

@Slf4j
@Service
public class ReckonerServiceV3 {

    private final ReckonerRepository reckonerRepository;
    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final LoanService loanService;
    private final LoanMapper loanMapper;

    @Autowired
    public ReckonerServiceV3(ReckonerService reckonerService, ReckonerRepository reckonerRepository, LoanRepository loanRepository, AccountRepository accountRepository, AccountService accountService, LoanService loanService, LoanMapper loanMapper) {
        this.reckonerRepository = reckonerRepository;
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
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

    public ReckonerLoanUnionPage getAllTransactionsCriteria(TransactionQueryCriteria criteria) {
        List<LoanEntity> loans = loanRepository.findCriteria(criteria);
        List<ReckonerEntity> reckoners = reckonerRepository.findCriteria(criteria);

        if (CollUtil.isNotEmpty(criteria.getTagsContains())) {
            // 找不到办法让querydsl 处理 jsonb contains, 特别是映射property是List<String>类型
            loans = loans.stream().filter(loan ->
                    criteria.getTagsContains().stream().allMatch(p -> loan.getTags().stream().anyMatch(l -> l.contains(p)))).toList();
            reckoners = reckoners.stream().filter(reckon ->
                    criteria.getTagsContains().stream().allMatch(p -> reckon.getTags().stream().anyMatch(l -> l.contains(p)))).toList();
        }
        ReckonerLoanUnionPage page = new ReckonerLoanUnionPage();
        List<LoanEntity> loanPage = new ArrayList<>();
        List<ReckonerEntity> reckonerPage = new ArrayList<>();
        page.setLoanPage(loanPage);
        page.setReckonerPage(reckonerPage);

        for (int i = 0, j = 0;
             CollUtil.isNotEmpty(loans)
             || CollUtil.isNotEmpty(reckoners)
                && i + j < criteria.getPageSize();
        ) {
            LoanEntity left = (i < loans.size()) ? loans.get(i) : null;
            ReckonerEntity right = (j < reckoners.size()) ? reckoners.get(j) : null;

            if (left != null && (right == null || left.getTransDate().isAfter(right.getTransDate()))) {
                loanPage.add(left);
                i++;
            } else if (right != null) {
                reckonerPage.add(right);
                j++;
            } else break;
        }
        page.setTotal(loans.size() + reckoners.size());
        page.setReckonerPageSize(reckonerPage.size());
        page.setLoanPageSize(loanPage.size());
        page.setPageNum(criteria.getPageNum());
        page.setPageSize(loanPage.size() + reckonerPage.size());
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

    // find by from acct id, status <> 'deleted', order by trans_date desc
    public List<Loan_AccountAggre> assetAggre() {
        List<LoanEntity> loans = loanRepository.findNonDeletedOrderByTransDateDesc();
        Map<UUID, List<LoanEntity>> byAccount = loans.stream().collect(Collectors.groupingBy(e -> e.getFromAcctEntity().getId()));

        List<Loan_AccountAggre> res = new ArrayList<>();
        byAccount.values().forEach(
                el -> {
                    LoanEntity ent;
                    if (CollUtil.isNotEmpty(el) && null != (ent = el.get(0))) {
                        Loan_AccountAggre accountAggre = new Loan_AccountAggre();
                        accountAggre.setAccountId(ent.getFromAcctEntity().getId());
                        accountAggre.setAccountName(ent.getFromAcctEntity().getName());
                        accountAggre.setCount(el.size());
                        accountAggre.setBillingCircle(ent.getFromAcctEntity().getBillingCycle());
                        LocalDate nearestDeadline = ent.getFromAcctEntity().getNearestDeadline();
                        accountAggre.setForcomingDeadline(nearestDeadline);
                        BigDecimal repayment = el.stream()
                                .flatMap(e -> e.getLoanScheduleEntitySet().stream().filter(s -> s.getDueDate().equals(nearestDeadline)))
                                .map(schedule ->
                                        (schedule.getPrincipal() != null ? schedule.getPrincipal() : BigDecimal.ZERO)
                                                .add(schedule.getInterest() != null ? schedule.getInterest() : BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        accountAggre.setForcomingPaymentAmount(repayment);

                        // set yearMonth Aggre list
                        Map<YearMonth, List<LoanEntity>> byMonth =
                                el.stream().collect(Collectors.groupingBy(
                                        e -> YearMonth.from(e.getTransDate()),
                                        LinkedHashMap::new,
                                        Collectors.toList()));
                        List<Loan_Account_YearMonthAggre> tmp = new ArrayList<>();
                        byMonth.forEach(
                                (k, v) -> {
                                    Loan_Account_YearMonthAggre account_yearMonthAggre = new Loan_Account_YearMonthAggre();
                                    account_yearMonthAggre.setYearMonth(k);
                                    account_yearMonthAggre.setSumPrincipal(v.stream().map(LoanEntity::getPrincipal).reduce(BigDecimal.ZERO, BigDecimal::add));
                                    account_yearMonthAggre.setSumInterest(v.stream().map(e -> Optional.ofNullable(e.getInterest()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add));
                                    account_yearMonthAggre.setCount(v.size());
                                    account_yearMonthAggre.setTransactions(v);
                                    tmp.add(account_yearMonthAggre);
                                }
                        );
                        accountAggre.setYearMonthAggres(tmp);
                        res.add(accountAggre);
                    }
                }
        );
        return res;
    }
}
