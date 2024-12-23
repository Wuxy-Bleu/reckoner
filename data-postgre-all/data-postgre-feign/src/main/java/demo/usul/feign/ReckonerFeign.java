package demo.usul.feign;

import demo.usul.dto.ReckonerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "postgresReckonerClient")
public interface ReckonerFeign {


    @GetMapping("/count/{fromAcct}")
    Long countByFromAcct(@PathVariable UUID fromAcct);

    @GetMapping("/{fromAcctName}")
    List<ReckonerDto> retrieveByFromAcctName(@PathVariable String fromAcctName);

    @GetMapping("")
    Page<ReckonerDto> retrieveAll(@RequestParam(defaultValue = "100") Integer pageSize,
                                  @RequestParam(defaultValue = "0") Integer pageNum);

    @GetMapping("/to/{name}")
    List<ReckonerDto> retrieveByToAcctName(@PathVariable String name);

    @PostMapping("")
    ReckonerDto createOne(@RequestBody ReckonerDto reckoner);

    @PostMapping("")
    ReckonerDto createOneNoTrigger(@RequestBody ReckonerDto reckoner, @RequestParam Boolean trigger);
}
