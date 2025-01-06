package demo.usul.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Loan_AccountAggre {

    private UUID accountId;

    private String accountName;

    private BigDecimal sumPrincipal;

    private BigDecimal sumInterest;

    private int count;

    private List<Loan_Account_YearMonthAggre> yearMonthAggres;

    private List<Loan_Account_BillingCricleAggre> billingCricleAggres;

    private String billingCircle;

    private LocalDate forcomingDeadline;

    private BigDecimal forcomingPaymentAmount;

    @Data
    @NoArgsConstructor
    public static class Loan_Account_YearMonthAggre {

        private YearMonth yearMonth;

        private BigDecimal sumPrincipal;

        private BigDecimal sumInterest;

        private int count;

        private List<LoanEntity> transactions;
    }

    @Data
    @NoArgsConstructor
    public static class Loan_Account_BillingCricleAggre {

        private LocalDate opening;

        private LocalDate closing;

        private BigDecimal sumPrincipal;

        private BigDecimal sumInterest;

        private int count;

        private LocalDate dueDate;
    }
}
