package demo.usul.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import static demo.usul.Const.SHANG_HAI;
import static demo.usul.dto.LoanScheduleDto.LoanScheduleStatus.PENDING;

@Getter
@Setter
@Entity
@Table(name = LoanScheduleEntity.TABLE_NAME, schema = "public")
public class LoanScheduleEntity {

    public static final String TABLE_NAME = "loan_schedule";
    public static final String COLUMN_ID_NAME = "id";
    public static final String COLUMN_LOANID_NAME = "loan_id";
    public static final String COLUMN_PRINCIPAL_NAME = "principal";
    public static final String COLUMN_INTEREST_NAME = "interest";
    public static final String COLUMN_CREATEDAT_NAME = "created_at";
    public static final String COLUMN_LASTUPDATEDAT_NAME = "last_updated_at";
    public static final String COLUMN_DUEDATE_NAME = "due_date";
    public static final String COLUMN_RECKONERID_NAME = "reckoner_id";
    public static final String COLUMN_STATUS_NAME = "status";

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

    @NotNull
    @ColumnDefault("now()")
    @Column(name = COLUMN_CREATEDAT_NAME, nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now(SHANG_HAI);

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = COLUMN_LASTUPDATEDAT_NAME, nullable = false)
    private OffsetDateTime lastUpdatedAt = OffsetDateTime.now(SHANG_HAI);

    @Column(name = COLUMN_DUEDATE_NAME)
    private LocalDate dueDate;

    @Column(name = COLUMN_RECKONERID_NAME)
    private UUID reckonerId;

    @NotNull
    @Column(name = COLUMN_STATUS_NAME, nullable = false, length = Integer.MAX_VALUE)
    private String status = PENDING.getStatus();

    @ManyToOne
    @JoinColumn(name = LoanScheduleEntity.COLUMN_LOANID_NAME)
    @JsonBackReference
    private LoanEntity loanEntity;
}