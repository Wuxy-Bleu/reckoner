package demo.usul.feign.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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

    private UUID id;

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