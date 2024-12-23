package demo.usul.dto;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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