package demo.usul.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TransactionQueryCriteria {

    private String fromAcctName;

    private UUID fromAcct;

    private List<String> tagsContains;

    private Integer transMonth;

    private Integer transYear;

    private Integer transWeek;

    private BigDecimal amountLargeThan;

    @NotNull
    private Integer pageNum;

    @NotNull
    private Integer pageSize;
}
