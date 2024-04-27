package demo.usul.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = Tag.TABLE_NAME, schema = "public")
public class Tag {

    public static final String TABLE_NAME = "tags";
    public static final String COLUMN_ID_NAME = "id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_CREATEDAT_NAME = "created_at";
    public static final String COLUMN_LASTUPDATEAT_NAME = "last_update_at";

    @Id
    @Column(name = COLUMN_ID_NAME, nullable = false)
    private UUID id;

    @Column(name = COLUMN_NAME_NAME, nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = COLUMN_CREATEDAT_NAME, nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = COLUMN_LASTUPDATEAT_NAME, nullable = false)
    private OffsetDateTime lastUpdateAt;

}