package demo.usul.repository;

import demo.usul.entity.ReckonerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface ReckonerRepository extends JpaRepository<ReckonerEntity, UUID>, JpaSpecificationExecutor<ReckonerEntity> {

    long countDistinctByFromAcctAllIgnoreCase(UUID fromAcct);

    List<ReckonerEntity> findByFromAcct(UUID fromAcct, Pageable pageable);




}