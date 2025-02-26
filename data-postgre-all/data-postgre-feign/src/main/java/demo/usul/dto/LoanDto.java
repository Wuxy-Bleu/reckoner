package demo.usul.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@NoArgsConstructor
public class LoanDto {

    private UUID id;
    private BigDecimal principal;
    private BigDecimal interest;
    private UUID fromAcct;
    private String currency;
    private BigDecimal toCny;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdatedAt;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private OffsetDateTime transDate;
    private String status;
    private String loanType;
    private String interestType;
    private Short installmentNumber;
    private String imageLink;
    private String descr;
    private List<String> tags;
    private Set<LoanScheduleDto> loanScheduleDtoSet;
    private AccountDto fromAcctDto;

    @Getter
    @AllArgsConstructor
    public enum LoanType {
        NO_INSTALLMENT("不分期"),
        INSTALLMENT("分期"),
        ADVANCED_CONSUMPTION("先用后付");

        private final String type;
    }

    @Getter
    @AllArgsConstructor
    public enum LoanStatus {
        ACTIVE("active"),
        PAID("paid"),
        DELETED("deleted");

        private final String status;
    }
}
