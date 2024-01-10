package demo.usul.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountModifyRecordDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -3259395283703310203L;

    private String uuid;

    private String lastUpdatedAt;

    private String createdAt;

    private String name;

    private BigDecimal balance;

    private String currency;

    private String cardType;

    private BigDecimal creditCardLimit;

    private String billingCycle;

    private String dueDate;

    private String comments;

}