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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static demo.usul.entity.AccountEntity.FIND_BY_IDS;
import static demo.usul.entity.AccountEntity.FIND_BY_NAME;
import static demo.usul.entity.AccountEntity.IF_ENTITIES_EXIST;
import static demo.usul.entity.AccountEntity.SOFT_DELETE;
import static demo.usul.utils.TimeUtil.compareDayOfMonthStringWithOffsetDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = AccountEntity.TABLE_NAME, schema = "public", indexes = {
        @Index(name = "accounts_pk2", columnList = "name", unique = true)
})
@NamedQuery(name = FIND_BY_IDS, query = "from AccountEntity where id in :ids and isActive = true ")
@NamedQuery(name = SOFT_DELETE, query = "update AccountEntity set isActive = false where id in :ids ")
@NamedQuery(name = IF_ENTITIES_EXIST, query = "from AccountEntity where isActive = false and id in :ids")
@NamedQuery(name = FIND_BY_NAME, query = "from AccountEntity where name = :name and isActive = true")
public class AccountEntity extends CommonColumn {

    public static final String FIND_BY_IDS = "acct_findByIds";
    public static final String FIND_BY_NAME = "acct_findByName";
    public static final String SOFT_DELETE = "soft_delete";
    public static final String IF_ENTITIES_EXIST = "if_entities_exist";
    public static final String TABLE_NAME = "accounts";
    public static final String COLUMN_ID_NAME = "id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_ISACTIVE_NAME = "is_active";
    public static final String COLUMN_TYPEID_NAME = "type_id";
    public static final String COLUMN_BALANCE_NAME = "balance";
    public static final String COLUMN_CURRENCY_NAME = "currency";
    public static final String COLUMN_CREDITCARDLIMIT_NAME = "credit_card_limit";
    public static final String COLUMN_BILLINGCYCLE_NAME = "billing_cycle";
    public static final String COLUMN_DUEDATE_NAME = "due_date";

    //    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = COLUMN_ID_NAME, nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = COLUMN_NAME_NAME, nullable = false)
    @NotNull
    @NotBlank
    private String name;

    @Column(name = COLUMN_ISACTIVE_NAME, nullable = false)
    @NotNull
    private Boolean isActive = true;

    @Column(name = COLUMN_BALANCE_NAME, nullable = false, precision = 18, scale = 8)
    @NotNull
    private BigDecimal balance = new BigDecimal(0);

    @Column(name = COLUMN_CURRENCY_NAME, nullable = false)
    @NotNull
    @NotBlank
    private String currency = "CNY";

    @Column(name = COLUMN_CREDITCARDLIMIT_NAME, precision = 18, scale = 8)
    private BigDecimal creditCardLimit;

    @Range(min = 1, max = 29)
    @Column(name = COLUMN_BILLINGCYCLE_NAME)
    private String billingCycle;

    @Range(min = 1, max = 29)
    @Column(name = COLUMN_DUEDATE_NAME)
    private String dueDate;

    @OneToMany(mappedBy = "fromAcctObj", fetch = FetchType.EAGER)
    private List<ReckonerEntity> outReckonerEntities;

    @OneToMany(mappedBy = "toAcctObj", fetch = FetchType.EAGER)
    private List<ReckonerEntity> inReckonerEntities;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = COLUMN_TYPEID_NAME, referencedColumnName = CardTypeEntity.COLUMN_ID_NAME, unique = true)
    private CardTypeEntity cardTypeEntity;

    public String getTypeName() {
        return getCardTypeEntity().getTypeName();
    }

    // 返回还款日，交易日期day of month < 入账周期的opening day of month, 也就是这笔交易在最近的还款日还清，else下一个还款日
    // 入账周期正好是月初到月末的话，还款日永远是下月的due date
    public LocalDate deadline(OffsetDateTime target) {
        int opening = Integer.parseInt(getBillingCycle().split("~+")[0]);
        int dealine = Integer.parseInt(getDueDate());
        if (1 == opening)
            return LocalDate.now().withDayOfMonth(dealine).plusMonths(1);
        boolean ifLatestDeadline = compareDayOfMonthStringWithOffsetDateTime(opening, target);
        if (ifLatestDeadline && dealine >= opening)
            return LocalDate.now().withDayOfMonth(dealine);
        else if (!ifLatestDeadline && dealine < opening)
            return LocalDate.now().withDayOfMonth(dealine).plusMonths(2);
        else
            return LocalDate.now().withDayOfMonth(dealine).plusMonths(1);

    }
}