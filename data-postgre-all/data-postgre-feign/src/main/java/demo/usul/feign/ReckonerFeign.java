package demo.usul.feign;

import demo.usul.dto.ReckonerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "postgresReckonerClient")
public interface ReckonerFeign {


    @GetMapping("/count/{fromAcct}")
    Long countByFromAcct(@PathVariable UUID fromAcct);

    @GetMapping("/{fromAcctName}")
    List<ReckonerDto> retrieveByFromAcctName(@PathVariable String fromAcctName);

    @GetMapping("")
    List<ReckonerDto> retrieveAll();

    @GetMapping("/to/{name}")
    List<ReckonerDto> retrieveByToAcctName(@PathVariable String name);
}
