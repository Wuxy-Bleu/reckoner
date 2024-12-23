package demo.usul.beans;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AcctsVo {

    private String name;
    private BigDecimal balance;
    private String cardType;
}
