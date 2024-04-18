package demo.usul.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import demo.usul.enums.InOutEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReckonerDto extends CommonColumn implements Serializable {

    @Serial
    private static final long serialVersionUID = -5953612981270616981L;

    private BigDecimal amount;

    private String currency;

    private InOutEnum inOut;

    private AccountDto fromAcctObj;

    private AccountDto toAcctObj;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private OffsetDateTime transDate;

    private UUID typeId;

    private ReckonerTypeDto reckonerTypeObj;

}
