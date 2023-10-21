package demo.usul.dto;

import demo.usul.entity.AccountEntity;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * DTO for {@link AccountEntity}
 */
@Data
@Builder
@AllArgsConstructor
public class AccountDto implements Serializable {

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