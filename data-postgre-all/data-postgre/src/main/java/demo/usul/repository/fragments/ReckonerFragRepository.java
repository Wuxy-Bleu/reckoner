package demo.usul.repository.fragments;

import demo.usul.entity.ReckonerEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReckonerFragRepository {

    void saveAssociations(ReckonerEntity entity);

    List<ReckonerEntity> findByFromAcctAndTagsOrderByTransDateDesc(UUID id, String s, String s1);

    List<ReckonerEntity> findByTagsOrderByTransDateDesc(String s, String s1);

    Map<String, List<ReckonerFragRepositoryImpl.Stat>> statsToAcc(UUID id);

    Map<String, List<ReckonerFragRepositoryImpl.Stat>> statsFromAcc(UUID id);
}
