package demo.usul.convert;

import demo.usul.entity.AccountEntity;
import demo.usul.dto.AccountDto;
import demo.usul.dto.AccountUpdateDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "cardType", source = "cardTypeEntity.typeName")
    AccountDto accountEntity2Dto(AccountEntity accountEntity);

    @InheritInverseConfiguration
    AccountEntity accountDto2Entity(AccountDto accountDto);

    List<AccountDto> accountEntities2Dtos(List<AccountEntity> accountsEntities);

    @InheritInverseConfiguration
    List<AccountEntity> accountDtos2Entities(List<AccountDto> accountDtos);

    @Mapping(target = "cardTypeEntity.typeName", source = "cardType")
    AccountEntity accountUpdateDto2Entity(AccountUpdateDto accountUpdateDto);

    List<AccountEntity> accountUpdateDtos2Entities(List<AccountUpdateDto> accountUpdateDtos);

    @Mapping(target = "name", source = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "balance", source = "balance", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "creditCardLimit", source = "creditCardLimit", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dueDate", source = "dueDate", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "billingCycle", source = "billingCycle", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountEntityFromAccountUpdateDto(AccountUpdateDto from, @MappingTarget AccountEntity to);
}
