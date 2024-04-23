package demo.usul.service;

import demo.usul.dto.AcctBlcCalculateDto;
import demo.usul.entity.AccountModifyRecord;
import demo.usul.repository.AccountModifyRepository;
import demo.usul.repository.AcctBlcCalculateRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcctModifyService {

    private final AccountModifyRepository accountModifyRepository;
    private final AcctBlcCalculateRepo acctBlcCalculateRepo;

    public void createAccountModifyRecord(AccountModifyRecord accountModifyRecord) {
        accountModifyRepository.insert(accountModifyRecord);
    }

    public List<AccountModifyRecord> retrieveModifyRecord(String uuid) {
        return accountModifyRepository.findByUuidAllIgnoreCase(uuid);
    }

    public void createAccountModifyRecords(List<AccountModifyRecord> accountModifyRecords) {
        accountModifyRepository.insert(accountModifyRecords);
    }

    public void saveOne(AcctBlcCalculateDto dto) {
        acctBlcCalculateRepo.insert(dto);
    }

    public void saveAll(List<AcctBlcCalculateDto> dtos) {
        acctBlcCalculateRepo.insert(dtos);
    }
}
