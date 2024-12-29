package demo.usul.repository.fragments;

import demo.usul.dto.TransactionQueryCriteria;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.ReckonerUnionQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface LoanFragRepository {

    ReckonerUnionQuery findAllTransactionsPageableUnion2Table(Pageable page);

    List<LoanEntity> findCriteria(TransactionQueryCriteria criteria);
}
