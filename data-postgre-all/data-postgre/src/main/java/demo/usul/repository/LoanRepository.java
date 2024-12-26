package demo.usul.repository;

import demo.usul.entity.LoanEntity;
import demo.usul.repository.fragments.LoanFragRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<LoanEntity, UUID>, JpaSpecificationExecutor<LoanEntity>, LoanFragRepository {


    @Query(value = "select * from loan " +
                   "where status <> 'deleted'" +
                   " order by trans_date desc, id desc limit :size", nativeQuery = true)
    List<LoanEntity> findOrderByTransDateAndIdLimitSize(int size);

    @Modifying
    @Query("update LoanEntity l set l.status = :status where l.id = :id and l.status <> :status")
    int updateStatusByIdAndStatusNot(@Param("id") UUID id, @Param("status") String status);

    @Query("select l from LoanEntity l where l.id in :ids and l.status <> 'deleted'")
    List<LoanEntity> findByIdIn(@Param("ids") Collection<UUID> ids);

    @Query("select l from LoanEntity l where l.fromAcctEntity.id = :id and l.status <> 'deleted' order by l.transDate DESC")
    List<LoanEntity> findByFromAcctEntity_IdOrderByTransDateDesc(@Param("id") UUID id);
}