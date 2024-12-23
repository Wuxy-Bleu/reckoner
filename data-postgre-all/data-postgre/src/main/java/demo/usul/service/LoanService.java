package demo.usul.service;

import demo.usul.convert.LoanMapper;
import demo.usul.dto.LoanCreateDto;
import demo.usul.dto.LoanDto;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.LoanScheduleEntity;
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

import static demo.usul.dto.LoanDto.LoanType.NO_INSTALLMENT;
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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Transactional
    public LoanEntity create(LoanCreateDto loanCreateDto) {
        LoanEntity loanEntity = loanMapper.loanCreate2LoanEntity(loanCreateDto);
        // fetch account
        AccountEntity fAcct = accountRepository.findById(loanEntity.getFromAcctEntity().getId()).get();
        loanEntity.setFromAcctEntity(fAcct);
        // set schedule
        LoanScheduleEntity schedule = new LoanScheduleEntity();
        schedule.setLoanEntity(loanEntity);
        loanEntity.add(schedule);
        // config schedule
        if (NO_INSTALLMENT.getType().equals(loanEntity.getLoanType()))
            schedule.setPrincipal(loanEntity.getPrincipal());
        schedule.setDueDate(fAcct.deadline(loanEntity.getTransDate()));
        // persist
        return loanRepository.save(loanEntity);
    }

    public List<LoanEntity> getAll() {
        return loanRepository.findAll();
    }

    @Transactional
    public String deleteLoan(UUID id) {
        int i = loanRepository.updateStatusByIdAndStatusNot(id, LoanDto.LoanStatus.DELETED.getStatus());
        log.info("{} record delete from loan table", i);
        int i1 = loanScheduleEntityRepository.updateStatusByLoanEntityAndStatusNot(DELETED.getStatus(), id);
        log.info("{} record deleted from table loan_schedule", i1);
        return MessageFormat.format("{0} loans and {1} loan_schedule deleted",i, i1);
    }
}
