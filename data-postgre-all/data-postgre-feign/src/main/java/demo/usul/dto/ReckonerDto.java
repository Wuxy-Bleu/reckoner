package demo.usul.dto;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReckonerDto extends CommonColumn implements Serializable {

    @Serial
    private static final long serialVersionUID = -5953612981270616981L;

    private BigDecimal amount;

    private String currency;

    private InOutEnum inOut;

    private AccountDto fromAcctObj;

    private AccountDto toAcctObj;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private OffsetDateTime transDate;

    private UUID typeId;

    private String descr;

    private List<String> tags;

    private ReckonerTypeDto reckonerTypeObj;

    @Getter
    @AllArgsConstructor
    public enum InOutEnum {
        IN(1),
        OUT(-1),
        TRANSFER(0),
        ADVANCED_CONSUMPTION(2);

        public final Integer inOut;

        public static InOutEnum fromValue(Short value) {
            for (InOutEnum el : InOutEnum.values()) {
                if (NumberUtil.equals(el.getInOut(), value)) {
                    return el;
                }
            }
            throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }

}
