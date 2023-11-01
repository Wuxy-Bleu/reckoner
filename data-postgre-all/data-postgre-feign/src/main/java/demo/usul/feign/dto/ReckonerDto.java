package demo.usul.feign.dto;

import demo.usul.feign.enums.InOutEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReckonerDto extends CommonColumn implements Serializable {

    @Serial
    private static final long serialVersionUID = -5953612981270616981L;


    private BigDecimal amount;

    private String currency;

    private InOutEnum inOut;

    private AccountDto fromAcctObj;

    private AccountDto toAcctObj;

    private OffsetDateTime transDate;

}
