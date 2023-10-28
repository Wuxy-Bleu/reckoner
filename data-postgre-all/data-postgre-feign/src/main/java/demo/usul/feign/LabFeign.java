package demo.usul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "data-postgres", path = "/lab/v1/")
public interface LabFeign {

    @GetMapping
    String greeting();
}
