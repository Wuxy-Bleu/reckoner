package demo.usul.feign.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -3394182567695477496L;

    private UUID id;

    private String name;

    private BigDecimal balance;

    private String cardType;

    private BigDecimal creditCardLimit;

    private String billingCycle;

    private String dueDate;
}
