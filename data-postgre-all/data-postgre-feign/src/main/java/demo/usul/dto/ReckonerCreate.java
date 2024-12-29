package demo.usul.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static demo.usul.Const.SHANG_HAI;

@Data
@NoArgsConstructor
public class ReckonerCreate {

    @NotNull
    private BigDecimal amount;

    private Short inOut = -1;

    private UUID fromAcct;

    private String fromAcctStr;

    private UUID toAcct;

    private String toAcctStr;

    private String currency = "CNY";

    private String descr;

    private List<String> tags;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime transDate = OffsetDateTime.now(SHANG_HAI);
}
