package demo.usul.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -5833592436585442860L;

    @NotNull(message = "======{errMsg}")
    private String name;

    private BigDecimal balance;

    @NotNull(message = "{errMsg}")
    private String currency;

    @NotNull
    private String cardType;

    private BigDecimal creditCardLimit;

    private String billingCycle;

    private String dueDate;
}