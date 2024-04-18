package demo.usul.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.lang.annotation.Documented;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Document("acct_blc_calculate_records")
public class AcctBlcCalculateDto {

    @Field(targetType = FieldType.STRING)
    private UUID acctId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal afterBlc;
    @Field(targetType = FieldType.STRING)
    private UUID afterRekn;
    @Field(targetType = FieldType.STRING)
    private OffsetDateTime transDate;
}