package demo.usul.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class CommonColumn {

    public static final String COLUMN_CREATEDAT_NAME = "created_at";

    public static final String COLUMN_LASTUPDATEDAT_NAME = "last_updated_at";

    @Column(name = COLUMN_CREATEDAT_NAME, insertable = false)
    private OffsetDateTime createdAt;

    @Column(name = COLUMN_LASTUPDATEDAT_NAME, insertable = false)
    private OffsetDateTime lastUpdatedAt;
}
