package demo.usul.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
public class LoanDto {

    private UUID id;
    private BigDecimal principal;
    private BigDecimal interest;
    private UUID fromAcct;
    private String currency;
    private BigDecimal toCny;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdatedAt;
    private OffsetDateTime transDate;
    private String status;
    private String loanType;
    private String interestType;
    private Short installmentNumber;
    private String imageLink;
    private String descr;
    private Map<String, Object> tags;
    private Set<LoanScheduleDto> loanScheduleEntitySet;

    @Getter
    @AllArgsConstructor
    public enum LoanType {
        NO_INSTALLMENT("不分期"),
        INSTALLMENT("分期");

        private final String type;
    }

    @Getter
    @AllArgsConstructor
    public enum LoanStatus {
        ACTIVE("active"),
        PAID("paid");

        private final String status;
    }
}
