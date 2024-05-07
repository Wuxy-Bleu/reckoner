package demo.usul.service;

import demo.usul.converter.AcctBlcCalculateConverter;
import demo.usul.dto.AcctBlcCalculateDto;
import demo.usul.entity.AccountModifyRecord;
import demo.usul.repository.AccountModifyRepository;
import demo.usul.repository.AcctBlcCalculateRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcctModifyService {

    private final AccountModifyRepository accountModifyRepository;
    private final AcctBlcCalculateRepo acctBlcCalculateRepo;
    private final AcctBlcCalculateConverter acctBlcCalculateConverter;

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

    /**
     * @param dto 只有需要被update的field non-null, 不然会更新额外的东西
     * @return updated document
     */
    public AcctBlcCalculateDto updateOne(AcctBlcCalculateDto dto) {
        AcctBlcCalculateDto byId = acctBlcCalculateRepo.findById(dto.getId()).orElseThrow();
        acctBlcCalculateConverter.updateAcctBlcCalculateDto(dto, byId);
        return acctBlcCalculateRepo.save(byId);
    }

    public List<AcctBlcCalculateDto> retrieve() {
        return acctBlcCalculateRepo.findAll();
    }

    public AcctBlcCalculateDto retrieveOne(UUID id) {
        return acctBlcCalculateRepo.findByAfterRekn(id);
    }
}
