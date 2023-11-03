package demo.usul.entity;

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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = ReckonerEntity.TABLE_NAME, schema = "public")
public class ReckonerEntity {

    public static final String TABLE_NAME = "reckoner";

    public static final String COLUMN_ID_NAME = "id";

    public static final String COLUMN_AMOUNT_NAME = "amount";

    public static final String COLUMN_INNOUT_NAME = "innout";

    public static final String COLUMN_FROMACCT_NAME = "from_acct";

    public static final String COLUMN_TOACCT_NAME = "to_acct";

    public static final String COLUMN_CURRENCY_NAME = "currency";

    public static final String COLUMN_TOCNY_NAME = "to_cny";

    public static final String COLUMN_ISALIVE_NAME = "is_alive";

    public static final String COLUMN_CHANGEFROM_NAME = "change_from";

    public static final String COLUMN_CREATEDAT_NAME = "created_at";

    public static final String COLUMN_LASTUPDATEDAT_NAME = "last_updated_at";

    public static final String COLUMN_TRANSDATE_NAME = "trans_date";

    public static final String COLUMN_TYPEID_NAME = "type_id";


    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = COLUMN_ID_NAME, nullable = false, updatable = false, unique = true)
    private UUID id;


    @Column(name = COLUMN_AMOUNT_NAME, nullable = false, precision = 18, scale = 8)
    private BigDecimal amount;

    @Column(name = COLUMN_INNOUT_NAME, nullable = false)
    private Short inOut;

    @Column(name = COLUMN_FROMACCT_NAME)
    private UUID fromAcct;

    @Column(name = COLUMN_TOACCT_NAME)
    private UUID toAcct;

    @Column(name = COLUMN_CURRENCY_NAME, nullable = false, length = Integer.MAX_VALUE)
    private String currency;

    @Column(name = COLUMN_TOCNY_NAME, precision = 18, scale = 8)
    private BigDecimal toCny;

    @Column(name = COLUMN_ISALIVE_NAME, nullable = false)
    private Boolean isAlive = false;

    @Column(name = COLUMN_CHANGEFROM_NAME)
    private UUID changeFrom;

    @Column(name = COLUMN_CREATEDAT_NAME, nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = COLUMN_LASTUPDATEDAT_NAME, nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @Column(name = COLUMN_TRANSDATE_NAME, nullable = false)
    private OffsetDateTime transDate;

    @Column(name = COLUMN_TYPEID_NAME)
    private UUID typeId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = COLUMN_TYPEID_NAME, nullable = false, unique = true, insertable = false, updatable = false)
    private ReckonerTypeEntity reckonerTypeObj;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_FROMACCT_NAME, nullable = false, unique = true, insertable = false, updatable = false)
    private AccountEntity fromAcctObj;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_TOACCT_NAME, insertable = false, updatable = false)
    private AccountEntity toAcctEntity;
}