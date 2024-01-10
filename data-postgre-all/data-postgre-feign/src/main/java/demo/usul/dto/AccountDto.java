package demo.usul.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.lang.annotation.Documented;
import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends CommonColumn implements Serializable {

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