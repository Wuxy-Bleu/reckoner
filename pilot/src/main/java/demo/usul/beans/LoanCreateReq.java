package demo.usul.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static cn.hutool.core.text.CharSequenceUtil.isNotBlank;
import static demo.usul.Const.SHANG_HAI_NOW;

@Data
public class LoanCreateReq {

    @NotNull
    private BigDecimal principal;

    private BigDecimal interest;

    private UUID fromAcct;

    private String fromAcctName;

    private String currency;

    private String status;

    private String loanType;

    private String interestType;

    private Short installmentNumber;

    private String descr;

    @JsonRawValue
    private String tags;

    // todo unit test
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime transDate = SHANG_HAI_NOW;

    @AssertTrue(message = "交易账户id或者名称至少有一个不为空")
    public boolean validate() {
        return fromAcct != null || isNotBlank(fromAcctName);
    }

}