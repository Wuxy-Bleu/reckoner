package demo.usul.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Transaction {

    private UUID id;

    private BigDecimal amount;

    private Short inNOut = -1;

    private UUID fromAcctId;

    private String fromAcct;

    private UUID toAcctId;

    private String toAcct;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private OffsetDateTime transDate;

    private String fromAcctType;

    private String toAcctType;

    private String descr;

    private String tags;

    private Boolean isLoan;

    private Integer installmentNum = 0;
}
