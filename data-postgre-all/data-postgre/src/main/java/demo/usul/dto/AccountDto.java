package demo.usul.dto;

import demo.usul.entity.AccountEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * DTO for {@link AccountEntity}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto implements Serializable {

    @Serial
    private static final long  serialVersionUID = -5833592436585442860L;

    @NotNull(message = "======{errMsg}")
    private String name;

    private BigDecimal balance;

    @NotNull(message = "{errMsg}")
    private String currency;

    private OffsetDateTime createdAt;

    private OffsetDateTime lastUpdatedAt;

    @NotNull
    private String cardType;


}