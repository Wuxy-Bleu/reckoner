package demo.usul.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@SuperBuilder(toBuilder = true)
@Entity
@Table(name = CardTypeEntity.TABLE_NAME, schema = "public")
public class CardTypeEntity extends CommonColumn {

    public static final String TABLE_NAME = "card_type";

    public static final String COLUMN_ID_NAME = "id";

    public static final String COLUMN_TYPENAME_NAME = "type_name";


    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = COLUMN_ID_NAME, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = COLUMN_TYPENAME_NAME, nullable = false)
    @NotNull
    @NotBlank
    private String typeName;
}