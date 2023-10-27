package demo.usul.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
//@Builder
public class ReckonerDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2066614562456423809L;

    private AccountDto fromAcct;

    private AccountDto toAcct;

    private BigDecimal amount;

    private String inOut;

    private String currency;

    private OffsetDateTime transDate;

    private OffsetDateTime lastUpdatedAt;

    private OffsetDateTime createdAt;

}
