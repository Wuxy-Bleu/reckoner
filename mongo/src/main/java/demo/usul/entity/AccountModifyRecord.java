package demo.usul.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("account_modify_record")
public class AccountModifyRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 2931766940644684139L;

    private String uuid;

    private String lastUpdatedAt;

    private String createdAt;

    private String name;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal balance;

    private String currency;

    private String cardType;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal creditCardLimit;

    private String billingCycle;

    private String dueDate;

    private String comments;

}
