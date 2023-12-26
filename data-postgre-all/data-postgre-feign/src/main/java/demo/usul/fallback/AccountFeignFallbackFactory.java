package demo.usul.fallback;

import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.feign.AccountFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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

        return new AccountFeign() {

            @Override
            public List<AccountDto> retrieveActivatedByConditionsOrNot(String type, String currency) {
                return null;
            }

            @Override
            public AccountDto retrieveActivatedByName(String name) {
                return null;
            }

            @Override
            public AccountDto createOne(AccountDto accountDto) {
                return null;
            }

            @Override
            public Integer delete(List<UUID> delIds) {
                return null;
            }

            @Override
            public Integer deleteById(UUID id) {
                return null;
            }

            @Override
            public List<AccountDto> update(List<AccountUpdateDto> accountUpdateDtos) {
                return null;
            }
        };
    }
}

