package demo.usul.feign.acc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "data-hnn", path = "/lab/v1/")
public interface LabFeign {

    @GetMapping
    String greeting();
}
