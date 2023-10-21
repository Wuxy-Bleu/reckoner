package demo.usul.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = ReckonerTypeEntity.TABLE_NAME, schema = "public")
public class ReckonerTypeEntity {

    public static final String TABLE_NAME = "reckoner_type";

    public static final String COLUMN_ID_NAME = "id";

    public static final String COLUMN_TYPENAME_NAME = "type_name";

    public static final String COLUMN_CREATEDAT_NAME = "created_at";

    public static final String COLUMN_LASTUPDATEAT_NAME = "last_update_at";


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = COLUMN_ID_NAME, nullable = false)
    private UUID id;

    @Column(name = COLUMN_TYPENAME_NAME, nullable = false, length = Integer.MAX_VALUE)
    private String typeName;

    @Column(name = COLUMN_CREATEDAT_NAME, nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = COLUMN_LASTUPDATEAT_NAME, nullable = false)
    private OffsetDateTime lastUpdateAt;

}