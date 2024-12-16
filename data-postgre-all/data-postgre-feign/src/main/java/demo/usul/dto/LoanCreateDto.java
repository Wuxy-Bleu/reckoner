package demo.usul.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static cn.hutool.core.text.CharSequenceUtil.isNotBlank;

@Data
public class LoanCreateDto {

    @NotNull
    private BigDecimal principal;

    private BigDecimal interest;

    private UUID fromAcct;

    private String fromAcctName;

    private String currency;

    private BigDecimal toCny;

    private String status;

    private String loanType;

    private String interestType;

    private Short installmentNumber;

    private String imageLink;

    private String descr;

    private List<String> tags;

    @AssertTrue(message = "交易账户id或者名称至少有一个不为空")
    public boolean validate() {
        return fromAcct != null || isNotBlank(fromAcctName);
    }

}