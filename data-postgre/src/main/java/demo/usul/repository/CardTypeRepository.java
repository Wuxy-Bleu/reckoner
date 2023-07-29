package demo.usul.repository;

import demo.usul.entity.CardTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTypeRepository extends JpaRepository<CardTypeEntity, Short> {
}
