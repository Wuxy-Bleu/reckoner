package demo.usul.repository;

import demo.usul.entity.AccountEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByNameIgnoreCase(@NonNull String name);

    Optional<List<AccountEntity>> findByIsActive(Boolean isActive);
}
