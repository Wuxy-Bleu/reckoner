package demo.usul.feign.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class AccountDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -5833592436585442860L;

    private UUID id;

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