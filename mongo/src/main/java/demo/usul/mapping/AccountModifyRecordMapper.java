package demo.usul.mapping;

import demo.usul.dto.AccountModifyRecordDto;
import demo.usul.entity.AccountModifyRecord;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountModifyRecordMapper {

    AccountModifyRecordDto accountModifyRecordEntity2Dto(AccountModifyRecord accountModifyRecord);

    AccountModifyRecord accountModifyRecordDto2Entity(AccountModifyRecordDto accountModifyRecordDto);

    List<AccountModifyRecordDto> accountModifyRecordEntities2Dtos(List<AccountModifyRecord> accountModifyRecords);

    List<AccountModifyRecord> accountModifyRecordDtos2Entities(List<AccountModifyRecordDto> accountModifyRecordDtos);
}
