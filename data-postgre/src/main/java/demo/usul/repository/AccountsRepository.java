package demo.usul.repository;

import demo.usul.entity.AccountsEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountsRepository extends JpaRepository<AccountsEntity, UUID> {

    @Nonnull
    @EntityGraph(value = "accounts-with-cardType")
    @Override
    List<AccountsEntity> findAll();

}
