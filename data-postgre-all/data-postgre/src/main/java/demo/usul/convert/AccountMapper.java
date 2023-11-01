package demo.usul.convert;

import demo.usul.feign.dto.AccountDto;
import demo.usul.entity.AccountEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "cardType", source = "cardTypeEntity.typeName")
    AccountDto accountEntity2Dto(AccountEntity accountEntity);

    @InheritInverseConfiguration
    AccountEntity accountDto2Entity(AccountDto accountDto);

    List<AccountDto> accountEntities2Dtos(List<AccountEntity> accountsEntities);

    @InheritInverseConfiguration
    List<AccountEntity> accountDtos2Entities(List<AccountDto> accountDtos);
}
