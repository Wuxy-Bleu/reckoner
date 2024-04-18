package demo.usul.service;

import demo.usul.convert.ReckonerMapper;
import demo.usul.dto.ReckonerTypeDto;
import demo.usul.entity.ReckonerTypeEntity;
import demo.usul.repository.ReckonerTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RcknTypeService {

    private final ReckonerTypeRepository repository;
    private final ReckonerMapper reckonerMapper;

    @Autowired
    public RcknTypeService(ReckonerTypeRepository repository, ReckonerMapper reckonerMapper) {
        this.repository = repository;
        this.reckonerMapper = reckonerMapper;
    }

    public ReckonerTypeDto createOne(ReckonerTypeDto dto) {
        ReckonerTypeEntity saved = repository.save(reckonerMapper.rcknDto2ENt(dto));
        return reckonerMapper.rcknEnt2Dto(repository.findById(saved.getId()).orElseThrow());
    }

    public List<ReckonerTypeDto> retrieveAll() {
        return reckonerMapper.rckEnts2Dtos(repository.findAll());
    }

    public List<ReckonerTypeDto> deleteByName(List<String> names) {
        return reckonerMapper.rckEnts2Dtos(repository.deleteByTypeNameInIgnoreCase(names));
    }
}
