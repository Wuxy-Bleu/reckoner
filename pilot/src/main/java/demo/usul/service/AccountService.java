package demo.usul.service;

import demo.usul.feign.acc.AccountFeign;
import demo.usul.feign.acc.LabFeign;
import demo.usul.feign.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final LabFeign labFeign;

    private final AccountFeign accountFeign;

    public List<AccountDto> xxx() {
        return accountFeign.retrieveActivatedByConditionsOrNot(null, null);
    }

}
