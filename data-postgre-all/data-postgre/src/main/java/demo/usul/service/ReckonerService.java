package demo.usul.service;

import demo.usul.entity.AccountEntity;
import demo.usul.entity.ReckonerEntity;
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

    public Long countDistinctByFromAcctAllIgnoreCase(UUID fromAcct) {
        return reckonerRepository.countDistinctByFromAcctAllIgnoreCase(fromAcct);
    }

    public List<ReckonerEntity> retrieveByName(String name) {
        AccountEntity acct = accountService.retrieveActivatedByName(name);
        return reckonerRepository.findByFromAcct(acct.getId(), Pageable.ofSize(10));
    }

    public List<ReckonerEntity> retrieveAll() {
        return reckonerRepository.findAll();
    }
}
