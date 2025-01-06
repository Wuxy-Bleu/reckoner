package demo.usul.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import demo.usul.convert.LoanMapper;
import demo.usul.dto.LoanCreateDto;
import demo.usul.dto.TransactionQueryCriteria;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.LoanScheduleEntity;
import demo.usul.entity.Loan_AccountAggre;
import demo.usul.entity.Loan_AccountAggre.Loan_Account_BillingCricleAggre;
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
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static cn.hutool.core.util.NumberUtil.add;
import static demo.usul.dto.LoanDto.LoanType.ADVANCED_CONSUMPTION;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

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
    // 如果是信用账户，重新insert一条loan+schdule记录
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
            loanCreateDto.setCol0(
                    Map.of("original_trans_date",
                            advancedConsumption.getTransDate().withOffsetSameInstant(ZoneOffset.ofHours(8)).format(ISO_OFFSET_DATE_TIME)));
            LoanEntity entity = loanService.create(loanCreateDto);
            Map<String, Object> json = new HashMap<>();
            json.put("loan", entity.getId());
            advancedConsumption.setCol0(json);
            return entity;
        }
        return null;
    }

    public List<ReckonerEntity> advancedConsumptions() {
        return reckonerRepository.findByInOutAndIsAliveTrueOrderByTransDateDesc((short) 2);
    }

    public List<Loan_AccountAggre> assetAggre() {
        // find by from acct id, status <> 'deleted', order by trans_date desc
        List<LoanEntity> loans = loanRepository.findNonDeletedOrderByTransDateDesc();
        Map<UUID, List<LoanEntity>> groupByAccount = loans.stream().collect(Collectors.groupingBy(e -> e.getFromAcctEntity().getId()));

        List<Loan_AccountAggre> res = new ArrayList<>();
        groupByAccount.values().forEach(el -> assembleNonListProperties(el, res));
        return res;
    }

    // 暂时不可能出现非人民币币种分期的情况 并且当前表设计也无法表现这种情况
    private void assembleNonListProperties(List<LoanEntity> loansOfAnAccount, List<Loan_AccountAggre> res) {
        LoanEntity ent;
        if (CollUtil.isNotEmpty(loansOfAnAccount) && null != (ent = loansOfAnAccount.get(0))) {
            // 距离now最近的还款日
            LocalDate nearestDeadline = ent.getFromAcctEntity().getNearestDeadline();

            Loan_AccountAggre accountAggre = new Loan_AccountAggre();
            accountAggre.setAccountId(ent.getFromAcctEntity().getId());
            accountAggre.setAccountName(ent.getFromAcctEntity().getName());
            accountAggre.setCount(loansOfAnAccount.size());
            accountAggre.setBillingCircle(ent.getFromAcctEntity().getBillingCycle());
            accountAggre.setForcomingDeadline(nearestDeadline);

            // 计算距离now最近的还款日 已累计的账单
            // loans record中过滤出 dueDate==离now最近还款日 的所有pending的schedules, 累加
            BigDecimal repayment = loansOfAnAccount.stream()
                    .flatMap(e -> e.getLoanScheduleEntitySet().stream().filter(s -> s.getDueDate().equals(nearestDeadline)))
                    .map(schedule -> add(schedule.getPrincipal(), schedule.getInterest()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            accountAggre.setForcomingPaymentAmount(repayment);

            // 除上面的信用账户基本信息之外，还会有两种聚合统计的方式 两者互不相关
            accountAggre.setBillingCricleAggres(assembleBillingCricles(loansOfAnAccount));
            accountAggre.setYearMonthAggres(assembleYearMonths(loansOfAnAccount));
            res.add(accountAggre);
        }
    }

    private List<Loan_Account_YearMonthAggre> assembleYearMonths(List<LoanEntity> loansOfAnAccount) {
        // 按照yearMonth分组所有交易，并且每组之间key asc
        Map<YearMonth, List<LoanEntity>> thenGroupByMonth =
                loansOfAnAccount.stream().collect(Collectors.groupingBy(
                        e -> YearMonth.from(e.getTransDate()), LinkedHashMap::new, Collectors.toList()));

        List<Loan_Account_YearMonthAggre> tmp = new ArrayList<>();
        thenGroupByMonth.forEach(
                (k, v) -> {
                    Loan_Account_YearMonthAggre account_yearMonthAggre = new Loan_Account_YearMonthAggre();
                    account_yearMonthAggre.setYearMonth(k);
                    account_yearMonthAggre.setSumPrincipal(
                            v.stream().map(e -> !CharSequenceUtil.equals("CNY", e.getCurrency()) ? e.getToCny() : e.getPrincipal()).reduce(BigDecimal.ZERO, BigDecimal::add));
                    account_yearMonthAggre.setSumInterest(
                            v.stream().map(e -> Optional.ofNullable(e.getInterest()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add));
                    account_yearMonthAggre.setCount(v.size());
                    account_yearMonthAggre.setTransactions(v);
                    tmp.add(account_yearMonthAggre);
                });
        return tmp;
    }

    private List<Loan_Account_BillingCricleAggre> assembleBillingCricles(List<LoanEntity> loansOfAnAccount) {
        List<Loan_Account_BillingCricleAggre> res = new ArrayList<>();

        loansOfAnAccount.stream()
                .flatMap(loan -> loan.getLoanScheduleEntitySet().stream())
                .collect(Collectors.groupingBy(LoanScheduleEntity::getDueDate, LinkedHashMap::new, Collectors.toList()))
                .forEach((k, v) -> {
                    Loan_Account_BillingCricleAggre tmp = new Loan_Account_BillingCricleAggre();
                    tmp.setDueDate(k);
                    //todo 这种有非人民币币种的记录 数据库表要改
                    tmp.setSumPrincipal(
                            v.stream().map(LoanScheduleEntity::getPrincipal).reduce(BigDecimal.ZERO, BigDecimal::add));
                    tmp.setSumInterest(
                            v.stream().map(e -> Optional.ofNullable(e.getInterest()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add));
                    tmp.setCount(v.size());
                    res.add(tmp);
                });
        res.sort(Comparator.comparing(Loan_Account_BillingCricleAggre::getDueDate));
        return res;
    }
}
