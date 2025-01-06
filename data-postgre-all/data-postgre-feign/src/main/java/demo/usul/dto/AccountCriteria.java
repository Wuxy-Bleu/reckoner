package demo.usul.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCriteria {

    public static final AccountCriteria EMPTY_CRITERIA = new AccountCriteria();
    private UUID id;
    private String name;
    private String cardType;
    private String currency;
}
