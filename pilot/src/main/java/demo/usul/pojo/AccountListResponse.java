package demo.usul.pojo;

import demo.usul.feign.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -5459756163710200005L;

    private List<AccountDto> accounts;

    private Integer count;

}
