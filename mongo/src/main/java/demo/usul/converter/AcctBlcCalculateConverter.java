package demo.usul.converter;

import demo.usul.dto.AcctBlcCalculateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AcctBlcCalculateConverter {


    void updateAcctBlcCalculateDto(AcctBlcCalculateDto from, @MappingTarget AcctBlcCalculateDto to);
}
