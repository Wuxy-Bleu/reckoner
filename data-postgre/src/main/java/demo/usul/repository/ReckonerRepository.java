package demo.usul.repository;

import demo.usul.entity.ReckonerEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ReckonerRepository extends JpaRepository<ReckonerEntity, UUID>, JpaSpecificationExecutor<ReckonerEntity> {

    @Override
    @Nonnull
//    @EntityGraph(value = "reckoner-with-type", attributePaths = "fromAcctEntity.cardTypeEntity")
    List<ReckonerEntity> findAll();
}