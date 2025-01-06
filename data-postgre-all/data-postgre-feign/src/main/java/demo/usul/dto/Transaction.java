package demo.usul.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Transaction {

    private UUID id;

    private BigDecimal amount;

    private String currency;

    private BigDecimal toCny;

    private Short inOut = -1;

    private UUID fromAcctId;

    private String fromAcct;

    private UUID toAcctId;

    private String toAcct;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private OffsetDateTime transDate;

    private String fromAcctType;

    private String toAcctType;

    private String descr;

    private List<String> tags;

    private Boolean isLoan;

    private List<BigDecimal> principals;

    private List<BigDecimal> interests;

    private List<LocalDate> dueDates;

    private Integer installmentNum;

    private String loanType;

    private Map<String, Object> loanCol0;
}
