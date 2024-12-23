package demo.usul.repository.fragments;

import demo.usul.entity.LoanEntity;
import demo.usul.entity.ReckonerUnionSubQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoanFragRepository {

    void create(LoanEntity loanEntity);

    List<ReckonerUnionSubQuery> findAllTransactionsPageableUnion2Table(Pageable page);
}
