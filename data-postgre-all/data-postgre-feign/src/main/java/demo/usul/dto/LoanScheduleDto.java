package demo.usul.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class LoanScheduleDto {

    private UUID id;

    private BigDecimal principal;

    private BigDecimal interest;

    private OffsetDateTime createdAt;

    private OffsetDateTime lastUpdatedAt;

    private LocalDate dueDate;

    private UUID reckonerId;

    private String status;

    @Getter
    @AllArgsConstructor
    public enum LoanScheduleStatus {
        PENDING("pending"),
        PAID("paid"),
        CLOSE("close");

        private final String status;
    }

}
