package demo.usul.repository.fragments;

import demo.usul.dto.TransactionQueryCriteria;
import demo.usul.entity.ReckonerEntity;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ReckonerFragRepository {

    void saveAssociations(ReckonerEntity entity);

    List<ReckonerEntity> findByFromAcctAndTagsOrderByTransDateDesc(UUID id, String s, String s1);

    List<ReckonerEntity> findByTagsOrderByTransDateDesc(String s, String s1);

    Map<String, List<ReckonerFragRepositoryImpl.Stat>> statsToAcc(UUID id);

    Map<String, List<ReckonerFragRepositoryImpl.Stat>> statsFromAcc(UUID id);

    List<ReckonerEntity> retrieveCondPageable(Optional<UUID> id, Optional<String> rcknType, Optional<Integer> inOut, Optional<Boolean> isOrderByTransDate, Optional<Boolean> isOrderByAmount, Pageable page);

    Long countCond(Optional<UUID> id, Optional<String> reckonerType, Optional<Integer> inOut);

    Collection<ReckonerFragRepositoryImpl.AccStat> statsGroupByAcc(Optional<Boolean> isMonthly,
                                                                   Optional<Boolean> isWeekly,
                                                                   Optional<Boolean> isGroupByType,
                                                                   Optional<String> acc,
                                                                   Optional<OffsetDateTime> timeBegin,
                                                                   Optional<OffsetDateTime> timeEnd);

    ReckonerEntity persist(ReckonerEntity reckoner);

    List<ReckonerEntity> findCriteria(TransactionQueryCriteria criteria);
}
