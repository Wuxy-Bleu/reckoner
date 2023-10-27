package demo.usul.service;

import demo.usul.entity.CardTypeEntity;
import demo.usul.repository.CardTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardTypeService {

    private final CardTypeRepository cardTypeRepository;

    public CardTypeEntity retrieveByNameUnique(String typeName) {
        return cardTypeRepository.findByTypeNameIgnoreCase(typeName).orElse(null);
    }

//    public CardTypeEntity createOrRetrieve(String typeName) {
//        CardTypeEntity entity = retrieveByNameUnique(typeName);
//        if (null == entity) {
//            CardTypeEntity toSave = new CardTypeEntity();
//            toSave.setTypeName(typeName);
//            return cardTypeRepository.save(toSave);
//        }
//        return entity;
//    }

}
