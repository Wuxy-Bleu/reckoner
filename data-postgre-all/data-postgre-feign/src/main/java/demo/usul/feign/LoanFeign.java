package demo.usul.feign;

import demo.usul.dto.LoanCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "postgresLoanClient")
public interface LoanFeign {

    @PostMapping()
    Object createLoan(@RequestBody LoanCreateDto loanCreateDto);


}
