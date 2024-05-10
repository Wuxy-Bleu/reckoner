package demo.usul.repository;

import demo.usul.entity.ReckonerEntity;
import demo.usul.repository.fragments.ReckonerFragRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ReckonerRepository extends JpaRepository<ReckonerEntity, UUID>, JpaSpecificationExecutor<ReckonerEntity>, ReckonerFragRepository {

    Long countDistinctByFromAcctAllIgnoreCase(UUID acct);

    List<ReckonerEntity> findByFromAcct(UUID fromAcct, Pageable pageable);

    Long countDistinctByToAcctAllIgnoreCase(UUID acct);

    List<ReckonerEntity> findByToAcct(UUID id, Pageable pageable);

    List<ReckonerEntity> findByToAcctOrderByTransDateDesc(UUID toAcct);

    @Transactional
    @Modifying
    @Query("update ReckonerEntity r set r.fromAcct = ?1 where r.id = ?2")
    int updateFromAcctById(UUID fromAcct, UUID id);

    ReckonerEntity findFirstByIsAliveOrderByCreatedAtDesc(Boolean isAlive);

    ReckonerEntity findFirstByIsAliveOrderByTransDateDesc(Boolean isAlive);

    @Query("select r from ReckonerEntity r where r.toAcct = ?1 order by r.inOut, r.typeId, r.transDate DESC")
    Page<ReckonerEntity> findByToAcctOrderByInOutAscTypeIdAscTransDateDesc(UUID toAcct, Pageable pageable);

    @Query("select r from ReckonerEntity r where r.toAcct = ?1 order by r.inOut desc, r.typeId, r.transDate DESC")
    Page<ReckonerEntity> findByToAcctOrderByInOutDescTypeIdAscTransDateDesc(UUID id, Pageable page);
//    @Query(value = "select r from ReckonerEntity r " +
//            "where r.fromAcct = :fromAcct " +
//            "and (r.tags::varchar ~ \':tags\' or r.tags::varchar ~ \':v_tags\')" +
//            "order by r.transDate DESC",
//            nativeQuery = true)
//    List<ReckonerEntity> findByFromAcctAndTagsOrderByTransDateDesc(@Param("fromAcct") UUID fromAcct, @Param("tags") String tags, @Param("v_tags") String vTags, Pageable pageable);
}