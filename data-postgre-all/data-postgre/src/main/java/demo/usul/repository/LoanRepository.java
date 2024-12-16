package demo.usul.repository;

import demo.usul.entity.LoanEntity;
import demo.usul.repository.fragments.LoanFragRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface LoanRepository extends JpaRepository<LoanEntity, UUID>, JpaSpecificationExecutor<LoanEntity>, LoanFragRepository {
}