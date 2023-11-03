package demo.usul.service;

import demo.usul.convert.ReckonerMapper;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.feign.dto.ReckonerDto;
import demo.usul.repository.ReckonerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReckonerService {

    private final ReckonerRepository reckonerRepository;

    private final AccountService accountService;

    private final ReckonerMapper reckonerMapper;

    public Long countDistinctByFromAcctAllIgnoreCase(UUID acct) {
        return reckonerRepository.countDistinctByFromAcctAllIgnoreCase(acct);
    }

    public Long countDistinctByToAcctAllIgnoreCase(UUID acct) {
        return reckonerRepository.countDistinctByToAcctAllIgnoreCase(acct);
    }

    public List<ReckonerEntity> retrieveByFromAcctName(String name) {
        AccountEntity acct = accountService.retrieveActivatedByName(name);
        return reckonerRepository.findByFromAcct(acct.getId(), Pageable.ofSize(10));
    }

    public List<ReckonerEntity> retrieveByToAcctName(String name) {
        AccountEntity acct = accountService.retrieveActivatedByName(name);
        return reckonerRepository.findByToAcct(acct.getId(), Pageable.ofSize(10));
    }

    public List<ReckonerEntity> retrieveAll() {
        return reckonerRepository.findAll();
    }

    public ReckonerDto createOne(ReckonerDto reckoner) {
        ReckonerEntity reckonerEntity = reckonerMapper.reckonerDto2Entity(reckoner);
        reckonerRepository.cre
    }
}
