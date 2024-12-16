package demo.usul.convert;

import demo.usul.dto.LoanCreateDto;
import demo.usul.entity.LoanEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true))
public interface LoanMapper {

    @Mapping(target = "fromAcctEntity.id", source = "fromAcct")
    LoanEntity loanCreate2LoanEntity(LoanCreateDto loanCreateDto);
}
