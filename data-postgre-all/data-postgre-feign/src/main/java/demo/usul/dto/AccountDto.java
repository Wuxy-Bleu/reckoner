package demo.usul.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends CommonColumn {

    @NotNull(message = "======{errMsg}")
    private String name;
    @NotNull
    private String cardType;
    private BigDecimal balance;
    @NotNull(message = "{errMsg}")
    private String currency;
    private BigDecimal creditCardLimit;
    private String billingCycle;
    private String dueDate;

    public AccountDto blcSubtract(BigDecimal num) {
        setBalance(getBalance().subtract(num));
        return this;
    }

    public AccountDto blcAdd(BigDecimal num) {
        setBalance(getBalance().add(num));
        return this;
    }
}