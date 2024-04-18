package demo.usul.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
