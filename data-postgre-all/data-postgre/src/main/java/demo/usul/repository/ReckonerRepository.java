package demo.usul.repository;

import demo.usul.entity.ReckonerEntity;
import demo.usul.feign.dto.ReckonerDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ReckonerRepository extends JpaRepository<ReckonerEntity, UUID>, JpaSpecificationExecutor<ReckonerEntity> {

    Long countDistinctByFromAcctAllIgnoreCase(UUID acct);

    List<ReckonerEntity> findByFromAcct(UUID fromAcct, Pageable pageable);

    Long countDistinctByToAcctAllIgnoreCase(UUID acct);

    List<ReckonerEntity> findByToAcct(UUID id, Pageable pageable);
}