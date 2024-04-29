package demo.usul.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("acct_blc_calculate_records")
public class AcctBlcCalculateDto {

    @MongoId
    private String id;
    @Field(targetType = FieldType.STRING)
    private UUID acctId;
    @Field(targetType = FieldType.STRING)
    private String acctName;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal beforeBlc;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal afterBlc;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal diff;
    @Field(targetType = FieldType.STRING)
    private UUID afterRekn;
    @Field(targetType = FieldType.TIMESTAMP)
    private OffsetDateTime transDate;
    @CreatedDate
    private OffsetDateTime creationDate;
    @LastModifiedDate
    private OffsetDateTime lastUpdateDate;
    @Version
    private Integer version;
}

