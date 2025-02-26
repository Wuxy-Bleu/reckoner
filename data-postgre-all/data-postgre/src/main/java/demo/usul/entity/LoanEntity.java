package demo.usul.entity;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static demo.usul.Const.SHANG_HAI;
import static demo.usul.dto.LoanDto.LoanStatus.ACTIVE;
import static demo.usul.dto.LoanDto.LoanType.NO_INSTALLMENT;

// not use @data on jpa entity
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
    public static final String COLUMN_COL0_NAME = "col0";

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
    private OffsetDateTime createdAt = OffsetDateTime.now(SHANG_HAI);

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = COLUMN_LASTUPDATEDAT_NAME, nullable = false, insertable = false)
    private OffsetDateTime lastUpdatedAt = OffsetDateTime.now(SHANG_HAI);

    @ColumnDefault("now()")
    @Column(name = COLUMN_TRANSDATE_NAME, nullable = false)
    private OffsetDateTime transDate = OffsetDateTime.now(SHANG_HAI);

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
    private List<LoanScheduleEntity> loanScheduleEntitySet = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = LoanEntity.COLUMN_FROMACCT_NAME)
    private AccountEntity fromAcctEntity;

    @Column(name = COLUMN_COL0_NAME)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> col0;

    public void add(LoanScheduleEntity entity) {
        getLoanScheduleEntitySet().add(entity);
    }

    // 根据installmentNumber来初始化相同个数的schedule entities, null or 0 schedules就是空list
    public void initSchedulesWithNoArgsConstructor() {
        for (
                int i = 0;
                (null != installmentNumber && i < installmentNumber) || (null == installmentNumber && i < 1);
                i++
        ) {
            LoanScheduleEntity schedule = new LoanScheduleEntity();
            schedule.setLoanEntity(this);
            this.add(schedule);
        }
    }

    // 顺序不重要，只要principals interests list中相同index的在同一个shcedule中
    public void setSchedulePrincipalsInterestsIterable(List<BigDecimal> principals, List<BigDecimal> interests) {
        for (int i = 0; CollUtil.isNotEmpty(principals) && i < installmentNumber; i++)
            loanScheduleEntitySet.get(i).setPrincipal(principals.get(i));
        for (int i = 0; CollUtil.isNotEmpty(interests) && i < installmentNumber; i++)
            loanScheduleEntitySet.get(i).setInterest(interests.get(i));
    }

    public void setScheduleDueDateFromFirstDeadline(LocalDate deadline) {
        if (null == installmentNumber)
            loanScheduleEntitySet.get(0).setDueDate(deadline);
        else {
            for (LoanScheduleEntity el : loanScheduleEntitySet) {
                el.setDueDate(deadline);
                deadline = deadline.plusMonths(1);
            }
        }
    }
}