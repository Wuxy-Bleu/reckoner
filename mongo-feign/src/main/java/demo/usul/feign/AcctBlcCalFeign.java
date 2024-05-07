package demo.usul.feign;

import demo.usul.dto.AcctBlcCalculateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient("mongo-acct-blc-client")
public interface AcctBlcCalFeign {

    @PostMapping
    void saveOne(@RequestBody AcctBlcCalculateDto dto);

    @GetMapping("/rckn/{id}")
    AcctBlcCalculateDto retrieveOne(@PathVariable UUID id);

    @PutMapping
    AcctBlcCalculateDto update(@RequestBody AcctBlcCalculateDto dto);
}
