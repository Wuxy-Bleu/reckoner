package demo.usul.feign;

import demo.usul.dto.AcctBlcCalculateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("mongo-acct-blc-client")
public interface AcctBlcCalFeign {

    @PostMapping
    void saveOne(@RequestBody AcctBlcCalculateDto dto);
}
