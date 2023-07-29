package demo.usul.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "card_type", schema = "public")
public class CardTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Short id;

    @Basic
    @Column(name = "type_name", nullable = false, length = -1)
    private String typeName;

    @Basic
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Basic
    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardTypeEntity that = (CardTypeEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(typeName, that.typeName) && Objects.equals(createdAt, that.createdAt) && Objects.equals(lastUpdatedAt, that.lastUpdatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeName, createdAt, lastUpdatedAt);
    }
}
