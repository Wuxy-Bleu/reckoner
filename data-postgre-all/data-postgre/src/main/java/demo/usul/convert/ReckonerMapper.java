package demo.usul.convert;

import demo.usul.dto.ReckonerDto;
import demo.usul.dto.ReckonerTypeDto;
import demo.usul.entity.ReckonerEntity;
import demo.usul.entity.ReckonerTypeEntity;
import demo.usul.enums.InOutEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReckonerMapper {

    @Named("inOut2Str")
    static InOutEnum inOut2Str(Short inOut) {
        if (inOut == 1) {
            return InOutEnum.IN;
        } else {
            if (inOut == 0)
                return InOutEnum.TRANSFER;
            return InOutEnum.OUT;
        }
    }

    @Named("inOutStr2Enum")
    static Short inOutStr2Enum(InOutEnum inOutEnum) {
        return switch (inOutEnum) {
            case OUT -> -1;
            case IN -> 1;
            case TRANSFER -> 0;
        };
    }

    @Mapping(source = "inOut", target = "inOut", qualifiedByName = "inOut2Str")
    ReckonerDto reckonerEntity2Dto(ReckonerEntity reckonerEntity);

    @Mapping(source = "inOut", target = "inOut", qualifiedByName = "inOutStr2Enum")
    ReckonerEntity reckonerDto2Entity(ReckonerDto reckonerDto);

    List<ReckonerDto> reckonerEntities2Dtos(List<ReckonerEntity> reckonerEntities);

    List<ReckonerEntity> reckonerDtos2Entities(List<ReckonerDto> reckonerDtos);

    ReckonerTypeDto rcknEnt2Dto(ReckonerTypeEntity reckonerTypeEntity);

    ReckonerTypeEntity rcknDto2ENt(ReckonerTypeDto reckonerTypeDto);

    List<ReckonerTypeDto> rckEnts2Dtos(List<ReckonerTypeEntity> entities);

    List<ReckonerTypeEntity> rckDtos2Ents(List<ReckonerTypeDto> dtos);
}
