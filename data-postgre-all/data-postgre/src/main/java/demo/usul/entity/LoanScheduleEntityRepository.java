package demo.usul.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LoanScheduleEntityRepository extends JpaRepository<LoanScheduleEntity, UUID> {

    @Modifying
    @Query("""
              update LoanScheduleEntity l set l.status = :status
            where l.loanEntity.id = :loanId and l.status <> :status""")
    int updateStatusByLoanEntityAndStatusNot(@Param("status") String status, @Param("loanId") UUID loanId);
}