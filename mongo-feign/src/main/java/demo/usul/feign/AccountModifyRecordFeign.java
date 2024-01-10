package demo.usul.feign;

import demo.usul.dto.AccountModifyRecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mongo-client")
public interface AccountModifyRecordFeign {

    @PostMapping("")
    void createModifyRecord(@RequestBody AccountModifyRecordDto accountModifyRecordDto);

    @GetMapping("/{uuid}")
    List<AccountModifyRecordDto> retrieveModifyRecord(@PathVariable String uuid);

    @PostMapping("/batch")
    void createModifyRecordBatch(@RequestBody List<AccountModifyRecordDto> accountModifyRecordDtos);
}
