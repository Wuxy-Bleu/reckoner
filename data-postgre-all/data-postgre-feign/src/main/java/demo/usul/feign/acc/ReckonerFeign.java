package demo.usul.feign.acc;

import demo.usul.feign.dto.ReckonerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "postgresReckonerClient")
public interface ReckonerFeign {


    @GetMapping("/count/{fromAcct}")
    public Long countByFromAcct(@PathVariable UUID fromAcct);

    @GetMapping("/{fromAcctName}")
    public List<ReckonerDto> retrieveByFromAcctName(@PathVariable String fromAcctName);

    @GetMapping("")
    public List<ReckonerDto> retrieveAll();

    @GetMapping("/to/{name}")
    public ResponseEntity<List<ReckonerDto>> retrieveByToAcctName(@PathVariable String name);
}
