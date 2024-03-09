package demo.usul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "zooClient")
public interface SnowFlakeFeign {

    @GetMapping
    public Long generateSnowFlakeId();
}
