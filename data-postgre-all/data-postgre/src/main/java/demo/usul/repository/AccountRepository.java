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

    @Modifying
    @Query("""
            update AccountEntity a
            set a.name = :name, a.balance = :balance, a.creditCardLimit = :creditCardLimit, a.billingCycle = :billingCycle, a.dueDate = :dueDate
            where a.id = :id and a.isActive = :isActive""")
    int updateNameAndBalanceAndCreditCardLimitAndBillingCycleAndDueDateByIdAndIsActive(@NonNull @Param("name") String name, @Param("balance") BigDecimal balance, @Param("creditCardLimit") BigDecimal creditCardLimit, @Param("billingCycle") String billingCycle, @Param("dueDate") String dueDate, @NonNull @Param("id") UUID id, @NonNull @Param("isActive") Boolean isActive);
}
