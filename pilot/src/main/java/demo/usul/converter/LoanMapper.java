package demo.usul.converter;

import demo.usul.beans.LoanCreateReq;
import demo.usul.dto.LoanCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LoanMapper {

    LoanCreateDto loanCreateReq2Dto(LoanCreateReq req);
}
