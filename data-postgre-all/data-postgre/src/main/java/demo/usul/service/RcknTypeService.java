package demo.usul.service;

import demo.usul.convert.ReckonerMapper;
import demo.usul.dto.ReckonerTypeDto;
import demo.usul.entity.ReckonerTypeEntity;
import demo.usul.repository.ReckonerRepository;
import demo.usul.repository.ReckonerTypeRepository;
import demo.usul.repository.fragments.RcknTypeFragRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RcknTypeService {

    private final ReckonerTypeRepository repository;
    private final ReckonerMapper reckonerMapper;
    private final ReckonerRepository reckonerRepository;

    @Autowired
    public RcknTypeService(ReckonerTypeRepository repository, ReckonerMapper reckonerMapper, ReckonerRepository reckonerRepository) {
        this.repository = repository;
        this.reckonerMapper = reckonerMapper;
        this.reckonerRepository = reckonerRepository;
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

    public List<RcknTypeFragRepositoryImpl.Stat> stat(Optional<Boolean> monthly, Optional<Boolean> weekly) {
        return repository.stat(monthly, weekly);
    }

    public List<RcknTypeFragRepositoryImpl.Stat> statWithType(String type, Optional<Boolean> isOrderByNumber, Optional<Short> inOut, Optional<Boolean> isCurrentMonth, Optional<Boolean> monthly, Optional<Boolean> weekly) {
        return repository.statWithType(type, isOrderByNumber, inOut, isCurrentMonth, monthly, weekly);
    }
}
