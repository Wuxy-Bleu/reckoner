package demo.usul.repository.fragments;

import demo.usul.entity.LoanEntity;
import demo.usul.entity.ReckonerUnionQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LoanFragRepository {

    ReckonerUnionQuery findAllTransactionsPageableUnion2Table(Pageable page);
}
