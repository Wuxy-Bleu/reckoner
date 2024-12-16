package demo.usul.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import demo.usul.Const;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static demo.usul.dto.LoanDto.LoanStatus.ACTIVE;
import static demo.usul.dto.LoanDto.LoanType.NO_INSTALLMENT;

@Getter
@Setter
@Entity
@Table(name = LoanEntity.TABLE_NAME, schema = "public")
public class LoanEntity {

    public static final String TABLE_NAME = "loan";
    public static final String COLUMN_ID_NAME = "id";
    public static final String COLUMN_PRINCIPAL_NAME = "principal";
    public static final String COLUMN_INTEREST_NAME = "interest";
    public static final String COLUMN_FROMACCT_NAME = "from_acct";
    public static final String COLUMN_CURRENCY_NAME = "currency";
    public static final String COLUMN_TOCNY_NAME = "to_cny";
    public static final String COLUMN_CREATEDAT_NAME = "created_at";
    public static final String COLUMN_LASTUPDATEDAT_NAME = "last_updated_at";
    public static final String COLUMN_TRANSDATE_NAME = "trans_date";
    public static final String COLUMN_STATUS_NAME = "status";
    public static final String COLUMN_LOANTYPE_NAME = "loan_type";
    public static final String COLUMN_INTERESTTYPE_NAME = "interest_type";
    public static final String COLUMN_INSTALLMENTNUMBER_NAME = "installment_number";
    public static final String COLUMN_IMAGELINK_NAME = "image_link";
    public static final String COLUMN_DESCR_NAME = "descr";
    public static final String COLUMN_TAGS_NAME = "tags";


    @NotNull
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = COLUMN_ID_NAME, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ColumnDefault("0")
    @Column(name = COLUMN_PRINCIPAL_NAME, nullable = false, precision = 18, scale = 8)
    private BigDecimal principal = BigDecimal.ZERO;

    @Column(name = COLUMN_INTEREST_NAME, precision = 18, scale = 8)
    private BigDecimal interest;

    @ColumnDefault("'CNY'")
    @Column(name = COLUMN_CURRENCY_NAME, length = Integer.MAX_VALUE)
    private String currency = "CNY";

    @Column(name = COLUMN_TOCNY_NAME, precision = 18, scale = 8)
    private BigDecimal toCny;

    @ColumnDefault("now()")
    @Column(name = COLUMN_CREATEDAT_NAME, nullable = false, insertable = false)
    private OffsetDateTime createdAt = Const.SHANG_HAI_NOW;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = COLUMN_LASTUPDATEDAT_NAME, nullable = false, insertable = false)
    private OffsetDateTime lastUpdatedAt = Const.SHANG_HAI_NOW;

    @ColumnDefault("now()")
    @Column(name = COLUMN_TRANSDATE_NAME, nullable = false)
    private OffsetDateTime transDate = Const.SHANG_HAI_NOW;

    @Column(name = COLUMN_STATUS_NAME, length = Integer.MAX_VALUE)
    private String status = ACTIVE.getStatus();

    @NotNull
    @Column(name = COLUMN_LOANTYPE_NAME, nullable = false, length = Integer.MAX_VALUE)
    private String loanType = NO_INSTALLMENT.getType();

    @Column(name = COLUMN_INTERESTTYPE_NAME, length = Integer.MAX_VALUE)
    private String interestType;

    @Column(name = COLUMN_INSTALLMENTNUMBER_NAME)
    private Short installmentNumber;

    @Column(name = COLUMN_IMAGELINK_NAME, length = Integer.MAX_VALUE)
    private String imageLink;

    @Column(name = COLUMN_DESCR_NAME, length = Integer.MAX_VALUE)
    private String descr;

    @Column(name = COLUMN_TAGS_NAME)
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tags;

    @OneToMany(mappedBy = "loanEntity", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<LoanScheduleEntity> loanScheduleEntitySet = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = LoanEntity.COLUMN_FROMACCT_NAME)
    private AccountEntity fromAcctEntity;

    public void add(LoanScheduleEntity entity) {
        getLoanScheduleEntitySet().add(entity);
    }

}