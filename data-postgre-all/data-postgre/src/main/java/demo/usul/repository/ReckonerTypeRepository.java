package demo.usul.repository;

import demo.usul.entity.ReckonerTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReckonerTypeRepository extends JpaRepository<ReckonerTypeEntity, UUID> {
}