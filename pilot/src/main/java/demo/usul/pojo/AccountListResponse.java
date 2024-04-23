package demo.usul.pojo;

import demo.usul.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountListResponse{
    private Integer totalCount;
    private Map<String, List<AccountDto>> data;
}
