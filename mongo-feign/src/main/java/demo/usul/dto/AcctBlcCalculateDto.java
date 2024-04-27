package demo.usul.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
//@Accessors(fluent = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("acct_blc_calculate_records")
public class AcctBlcCalculateDto {

    @Field(targetType = FieldType.STRING)
    private UUID acctId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal beforeBlc;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal afterBlc;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal diff;
    @Field(targetType = FieldType.STRING)
    private UUID afterRekn;
    @Field(targetType = FieldType.STRING)
    private OffsetDateTime transDate;

}
