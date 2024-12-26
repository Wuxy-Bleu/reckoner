package demo.usul.repository;

import demo.usul.entity.AccountEntity;
import demo.usul.repository.fragments.AcctFragRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID>, QuerydslPredicateExecutor<AccountEntity>, AcctFragRepository {

    @Query("select a from AccountEntity a where a.id in :ids")
    List<AccountEntity> findByIdIn(@Param("ids") @NonNull Collection<UUID> ids);

    Optional<AccountEntity> findByNameIgnoreCase(@NonNull String name);

    Optional<List<AccountEntity>> findByIsActive(Boolean isActive);

    @Transactional
    @Modifying
    @Query("update AccountEntity a set a.dueDate = ?1 where a.id = ?2")
    int updateDueDateById(String dueDate, @NonNull UUID id);

    @Transactional
    @Modifying
    @Query("update AccountEntity a set a.balance = ?1 where a.id = ?2 and a.isActive = ?3")
    int updateBalanceByIdAndIsActive(BigDecimal balance, UUID id, Boolean isActive);

    @Query("select a from AccountEntity a where a.id = :id and a.isActive = true")
    AccountEntity findByIdAndIsActiveTrue(@Param("id") UUID id);
}
