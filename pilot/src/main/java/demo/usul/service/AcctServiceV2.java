package demo.usul.service;

import demo.usul.feign.AccountFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AcctServiceV2 {

    private final AccountFeign accountFeign;

    public AcctServiceV2(AccountFeign accountFeign) {this.accountFeign = accountFeign;}

    public Map<String, String> assets() {
        return accountFeign.allMyMoney();
    }
}
