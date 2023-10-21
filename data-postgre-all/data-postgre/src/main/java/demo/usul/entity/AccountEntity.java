package demo.usul.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = AccountEntity.TABLE_NAME, schema = "public", indexes = {
        @Index(name = "accounts_pk2", columnList = "name", unique = true)
})
public class AccountEntity {

    public static final String TABLE_NAME = "accounts";

    public static final String COLUMN_ID_NAME = "id";

    public static final String COLUMN_NAME_NAME = "name";

    public static final String COLUMN_CREATEDAT_NAME = "created_at";

    public static final String COLUMN_LASTUPDATEDAT_NAME = "last_updated_at";

    public static final String COLUMN_ISACTIVE_NAME = "is_active";

    public static final String COLUMN_TYPEID_NAME = "type_id";

    public static final String COLUMN_BALANCE_NAME = "balance";

    public static final String COLUMN_CURRENCY_NAME = "currency";

    public static final String COLUMN_CREDITCARDLIMIT_NAME = "credit_card_limit";

    public static final String COLUMN_BILLINGCYCLE_NAME = "billing_cycle";

    public static final String COLUMN_DUEDATE_NAME = "due_date";


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = COLUMN_ID_NAME, nullable = false)
    private UUID id;

    @Column(name = COLUMN_NAME_NAME, nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = COLUMN_CREATEDAT_NAME, nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = COLUMN_LASTUPDATEDAT_NAME, nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @Column(name = COLUMN_ISACTIVE_NAME, nullable = false)
    private Boolean isActive = false;

    @Column(name = COLUMN_TYPEID_NAME, nullable = false)
    private Short typeId;

    @Column(name = COLUMN_BALANCE_NAME, nullable = false, precision = 18, scale = 8)
    private BigDecimal balance;

    @Column(name = COLUMN_CURRENCY_NAME, nullable = false, length = Integer.MAX_VALUE)
    private String currency;

    @Column(name = COLUMN_CREDITCARDLIMIT_NAME, precision = 18, scale = 8)
    private BigDecimal creditCardLimit;

    @Column(name = COLUMN_BILLINGCYCLE_NAME, length = Integer.MAX_VALUE)
    private String billingCycle;

    @Column(name = COLUMN_DUEDATE_NAME, length = Integer.MAX_VALUE)
    private String dueDate;

    @OneToMany(mappedBy = "fromAcctEntity")
    private List<ReckonerEntity> outReckonerEntities;

    @OneToMany(mappedBy = "toAcctEntity")
    private List<ReckonerEntity> inReckonerEntities;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_TYPEID_NAME, insertable = false, updatable = false)
    private CardTypeEntity cardTypeEntity;

}