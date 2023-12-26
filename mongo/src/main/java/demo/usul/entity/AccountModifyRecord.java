package demo.usul.entity;

import demo.usul.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Getter
@Setter
@Document
@NoArgsConstructor
@AllArgsConstructor
public class AccountModifyRecord {

    private AccountDto previous;
    private AccountDto current;
    private OffsetDateTime modifyDate;

}
