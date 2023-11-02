package demo.usul.service;

import demo.usul.feign.acc.ReckonerFeign;
import demo.usul.feign.dto.ReckonerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReckonerService {

    private final ReckonerFeign reckonerFeign;

    public Long countDistinctByFromAcctAllIgnoreCase(UUID fromAcct) {
        return reckonerFeign.countByFromAcct(fromAcct);
    }

    public List<ReckonerDto> retrieveByFromAcctName(String fromAcctName) {
        return reckonerFeign.retrieveByFromAcctName(fromAcctName);
    }

    public List<ReckonerDto> retrieveByToAcctName(String name) {
        return reckonerFeign.retrieveByToAcctName(name);
    }
}
