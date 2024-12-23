package demo.usul.converter;

import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountModifyRecordDto;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.beans.AcctsVo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    List<AccountModifyRecordDto> accountDto2ModifyRecordDtos(List<AccountDto> accountDtos);

    List<AccountModifyRecordDto> accountUpdateDtos2ModifyRecordDtos(List<AccountUpdateDto> accountUpdateDtos);

    default String mapDateToString(OffsetDateTime date) {
        return null == date ? null : date.toString();
    }

    AcctsVo acctDto2VO(AccountDto accountDto);
}
