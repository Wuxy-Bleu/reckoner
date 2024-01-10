package demo.usul.service;

import demo.usul.entity.AccountModifyRecord;
import demo.usul.repository.AccountModifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountModifyRecordService {

    private final AccountModifyRepository accountModifyRepository;

    public void createAccountModifyRecord(AccountModifyRecord accountModifyRecord) {
        accountModifyRepository.insert(accountModifyRecord);
    }

    public List<AccountModifyRecord> retrieveModifyRecord(String uuid) {
        return accountModifyRepository.findByUuidAllIgnoreCase(uuid);
    }

    public void createAccountModifyRecords(List<AccountModifyRecord> accountModifyRecords) {
        accountModifyRepository.insert(accountModifyRecords);
    }

}
