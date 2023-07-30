package demo.usul.entity;

import demo.usul.anno.JpaExclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts", schema = "public")
@ToString
public class AccountsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    @JpaExclude
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @JpaExclude
    private OffsetDateTime createdAt;

    @Column(name = "last_updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @JpaExclude
    private OffsetDateTime lastUpdatedAt;

    @Column(name = "is_active", nullable = false)
    @JpaExclude
    private boolean isActive;

    @Column(name = "type_id", nullable = false)
    @JpaExclude
    private short typeId;

    @Column(name = "balance", nullable = false, precision = 18, scale = 8)
    private BigDecimal balance;

    @Column(name = "currency", nullable = false, length = -1)
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    @ToString.Exclude
    @JpaExclude
    private CardTypeEntity cardTypeEntity;

    @Column(name = "credit_card_limit", precision = 18, scale = 8)
    private BigDecimal creditCardLimit;

    @Column(name = "billing_cycle")
    private String billingCycle;

    @Column(name = "due_date")
    private String dueDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private CardTypeEntity cardTypeEntity2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccountsEntity that = (AccountsEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}



