package demo.usul.beans;

import demo.usul.dto.AccountDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CachedAcctsDto {

    private boolean hit = true;
    private List<AccountDto> cached = new ArrayList<>();

    public CachedAcctsDto addAcctDtos(List<AccountDto> dtos) {
        cached.addAll(dtos);
        return this;
    }

    public CachedAcctsDto addAcctDto(AccountDto dto) {
        cached.add(dto);
        return this;
    }

    public CachedAcctsDto notHit() {
        hit = false;
        return this;
    }
}
