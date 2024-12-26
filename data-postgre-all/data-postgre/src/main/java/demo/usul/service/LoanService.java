package demo.usul.service;

import demo.usul.convert.LoanMapper;
import demo.usul.dto.LoanCreateDto;
import demo.usul.dto.LoanDto;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.LoanScheduleEntityRepository;
import demo.usul.repository.AccountRepository;
import demo.usul.repository.LoanRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import static demo.usul.dto.LoanDto.LoanType.INSTALLMENT;
import static demo.usul.dto.LoanScheduleDto.LoanScheduleStatus.DELETED;

@Slf4j
@Service
public class LoanService {

    private final LoanScheduleEntityRepository loanScheduleEntityRepository;

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final AccountRepository accountRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper, AccountRepository accountRepository,
                       LoanScheduleEntityRepository loanScheduleEntityRepository) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.accountRepository = accountRepository;
        this.loanScheduleEntityRepository = loanScheduleEntityRepository;
    }

    /**
     * 先用后付和不分期的loan唯一的区别就是 loan_schedule的due_date为空, 支付账号目前设置为优先级最高的交行信用卡
     * 先用后付会在一周内发邮件提醒用户，是否形成了确定的交易明细，更新支付账号，将原先的schedule记录设为closed，新增一条schedule pending记录
     * （但是真正的trans_date没法记录啊... 更新loan原先的trans_date吗，那么购买时间信息就丢失了
     * 还有如果是从非信用账户扣款, 我需要设置一条新的reckoner记录，那这条reckoner和之前的loan如何对应起来呢）
     * 最好的做法给这类付款一张新表？？
     * （目前做法，写入reckoner表，给reckoner新增col无名列（in or our列一个新的数字代表先用后付），
     * 当交易形成，新增一条loan或者reckoner, 新增的id存在col1中，然后旧reckoner is_alive=false）
     *
     * @param loanCreateDto 前端传过来的post报文
     * @return 插入的loanEntity
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Transactional
    public LoanEntity create(LoanCreateDto loanCreateDto) {
        LoanEntity loanEntity = loanMapper.loanCreate2LoanEntity(loanCreateDto);
        // fetch account
        AccountEntity fAcct = accountRepository.findById(loanEntity.getFromAcctEntity().getId()).get();
        loanEntity.setFromAcctEntity(fAcct);
        // set schedule
        loanEntity.initSchedulesWithNoArgsConstructor();
        // config schedule
        if (INSTALLMENT.getType().equals(loanEntity.getLoanType()))
            loanEntity.setSchedulePrincipalsInterestsIterable(loanCreateDto.getPrincipals(), loanCreateDto.getInterests());
        else
            loanEntity.getLoanScheduleEntitySet().get(0).setPrincipal(loanEntity.getPrincipal());
        loanEntity.setScheduleDueDateWithFirstDate(fAcct.deadline(loanEntity.getTransDate()));
        // persist
        return loanRepository.save(loanEntity);
    }

    // all including deleted
    public List<LoanEntity> getAll() {
        return loanRepository.findAll();
    }

    // delete one record including associated schedules
    @Transactional
    public String deleteLoan(UUID id) {
        int i = loanRepository.updateStatusByIdAndStatusNot(id, LoanDto.LoanStatus.DELETED.getStatus());
        log.info("{} record delete from loan table", i);
        int i1 = loanScheduleEntityRepository.updateStatusByLoanEntityAndStatusNot(DELETED.getStatus(), id);
        log.info("{} record deleted from table loan_schedule", i1);
        return MessageFormat.format("{0} loans and {1} loan_schedule deleted", i, i1);
    }


    public List<LoanEntity> getConditional(UUID fromAcct) {
        return loanRepository.findByFromAcctEntity_IdOrderByTransDateDesc(fromAcct);
    }
}
