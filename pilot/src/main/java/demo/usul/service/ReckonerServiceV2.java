package demo.usul.service;

import demo.usul.dto.ReckonerDto;
import demo.usul.feign.ReckonerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReckonerServiceV2 {

    private final ReckonerFeign reckonerFeign;

    public ReckonerServiceV2(ReckonerFeign reckonerFeign) {
        this.reckonerFeign = reckonerFeign;
    }

    public void retrieveAllTransactions(int pageSize, int pageNum) {
        Page<ReckonerDto> reckonerDtos = reckonerFeign.retrieveAll(pageSize, pageNum);

    }
}
