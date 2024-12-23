package demo.usul.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
//@SqlResultSetMapping(
//        name = "ReckonerUnionSubQueryMapping",
//        classes = @ConstructorResult(
//                targetClass = ReckonerUnionSubQuery.class,
//                columns = {
//                        @ColumnResult(name = "id", type = UUID.class),
//                        @ColumnResult(name = "trans_date", type = OffsetDateTime.class),
//                        @ColumnResult(name = "source", type = String.class)
//                }
//        )
//)
public class ReckonerUnionSubQuery {

    private UUID id;
    private OffsetDateTime transDate;
    private String source;
}
