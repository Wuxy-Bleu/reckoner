package demo.usul.convert;

import demo.usul.dto.ReckonerCreate;
import demo.usul.dto.ReckonerDto;
import demo.usul.dto.ReckonerTypeDto;
import demo.usul.entity.ReckonerEntity;
import demo.usul.entity.ReckonerTypeEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static demo.usul.dto.ReckonerDto.InOutEnum.fromValue;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true))
public interface ReckonerMapper {

    @Named("short2Enum")
    static ReckonerDto.InOutEnum short2Enum(Short inOut) {
        return fromValue(inOut);
    }

    @Named("enum2short")
    static Short enum2short(ReckonerDto.InOutEnum inOutEnum) {
        return inOutEnum.getInOut().shortValue();
    }

    @Mapping(source = "inOut", target = "inOut", qualifiedByName = "short2Enum")
    ReckonerDto reckonerEntity2Dto(ReckonerEntity reckonerEntity);

    @Mapping(source = "inOut", target = "inOut", qualifiedByName = "enum2short")
    ReckonerEntity reckonerDto2Entity(ReckonerDto reckonerDto);

    List<ReckonerDto> reckonerEntities2Dtos(List<ReckonerEntity> reckonerEntities);

    List<ReckonerEntity> reckonerDtos2Entities(List<ReckonerDto> reckonerDtos);

    ReckonerTypeDto rcknEnt2Dto(ReckonerTypeEntity reckonerTypeEntity);

    ReckonerTypeEntity rcknDto2ENt(ReckonerTypeDto reckonerTypeDto);

    List<ReckonerTypeDto> rckEnts2Dtos(List<ReckonerTypeEntity> entities);

    List<ReckonerTypeEntity> rckDtos2Ents(List<ReckonerTypeDto> dtos);

    ReckonerEntity reckonerCreate2ReckonerEntity(ReckonerCreate reckonerCreate);
}
