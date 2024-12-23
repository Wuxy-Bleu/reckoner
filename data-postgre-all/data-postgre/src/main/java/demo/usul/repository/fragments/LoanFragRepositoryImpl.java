package demo.usul.repository.fragments;

import demo.usul.entity.LoanEntity;
import demo.usul.entity.ReckonerUnionSubQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static demo.usul.Const.SHANG_HAI;

public class LoanFragRepositoryImpl implements LoanFragRepository {

    @PersistenceContext
    private EntityManager em;

    public void create(LoanEntity loanEntity) {
        return;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ReckonerUnionSubQuery> findAllTransactionsPageableUnion2Table(Pageable page) {
        Query nativeQuery = em.createNativeQuery(
                        """
                                select * from (
                                (select r.id, r.trans_date, 'a' as source from reckoner r order by r.trans_date, r.id desc limit :pageSize)
                                union all
                                (select l.id, l.trans_date, 'b' as source from loan l order by l.trans_date, l.id desc limit :pageSize)
                                ) comb order by trans_date, id desc limit :pageSize
                                """
                ).unwrap(org.hibernate.query.Query.class)
                .setTupleTransformer(
                        (tuple, aliases) -> {
                            ReckonerUnionSubQuery target = new ReckonerUnionSubQuery();
                            target.setId((UUID) tuple[0]);
                            target.setTransDate( OffsetDateTime.ofInstant((Instant) tuple[1], SHANG_HAI));
                            target.setSource((String) tuple[2]);
                            return target;
                        }
                );
        nativeQuery.setParameter("pageSize", page.getPageSize());
        //noinspection unchecked
        return nativeQuery.getResultList();
    }

}
