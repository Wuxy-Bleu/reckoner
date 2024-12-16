package demo.usul.pojo;

import cn.hutool.core.collection.CollUtil;
import demo.usul.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AcctsCachedDto {

    private int size;
    private List<AccountDto> accts;

    public static AcctsCachedDto fromAcctsDto(List<AccountDto> dtos) {
        return new AcctsCachedDto(CollUtil.isEmpty(dtos) ? 0 : dtos.size(), dtos);
    }
}
