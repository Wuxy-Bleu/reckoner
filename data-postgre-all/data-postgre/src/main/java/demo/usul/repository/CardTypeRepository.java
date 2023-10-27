package demo.usul.repository;

import demo.usul.entity.CardTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CardTypeRepository extends JpaRepository<CardTypeEntity, Short> {

    Optional<CardTypeEntity> findByTypeNameIgnoreCase(@NonNull String typeName);
}
