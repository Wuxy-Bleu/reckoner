//package demo.usul.mapper;
//
//import demo.usul.dto.AccountDto;
//import demo.usul.pojo.AcctsCachedDto;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//import org.mapstruct.ReportingPolicy;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring",
//        unmappedTargetPolicy = ReportingPolicy.IGNORE,
//        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface CacheMapper {
//
//    @Mapping(target = "accts", source = "dtos")
//    AcctsCachedDto acctsDtos2AcctsCachedDto(List<AccountDto> dtos);
//
//    default AccountDto mapAccountFromList(List<AccountDto> dtos) {
//        // Example: return the first element, or null if the list is empty
//        return (dtos != null && !dtos.isEmpty()) ? dtos.get(0) : null;
////    }
//}
