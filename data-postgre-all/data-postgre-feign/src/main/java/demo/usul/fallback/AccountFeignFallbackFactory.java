package demo.usul.fallback;

import demo.usul.feign.AccountFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

// not working
@Slf4j
@Component
public class AccountFeignFallbackFactory implements FallbackFactory<AccountFeign> {

    public AccountFeignFallbackFactory() {
        log.info("xxx");
    }

    @Override
    public AccountFeign create(Throwable cause) {
        log.info("xxx");

        return null;
    }
}

