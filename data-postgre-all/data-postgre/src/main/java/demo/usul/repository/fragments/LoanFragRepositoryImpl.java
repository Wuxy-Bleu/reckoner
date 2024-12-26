package demo.usul.repository.fragments;

import demo.usul.entity.ReckonerUnionQuery;
import demo.usul.entity.ReckonerUnionQuery.Union;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.Query;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static demo.usul.Const.SHANG_HAI;

@SuppressWarnings("unchecked")
public class LoanFragRepositoryImpl implements LoanFragRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public ReckonerUnionQuery findAllTransactionsPageableUnion2Table(Pageable page) {
        String sql = """
                select %s from (
                (select r.id, r.trans_date, 'a' as source from reckoner r where r.is_alive = true)
                union all
                (select l.id, l.trans_date, 'b' as source from loan l where l.status <> 'deleted')
                ) comb %s
                """;
        Integer total = (Integer) em.createNativeQuery(String.format(sql, "count(*)", " "), Integer.class).getSingleResult();
        Query<Union> nativeQuery = em.createNativeQuery(String.format(sql, "*", "order by trans_date desc, id desc limit :pageSize offset :offset"))
                .unwrap(Query.class)
                .setTupleTransformer((tuple, aliases) -> {
                    Union target = new Union();
                    target.setId((UUID) tuple[0]);
                    target.setTransDate(OffsetDateTime.ofInstant((Instant) tuple[1], SHANG_HAI));
                    target.setSource((String) tuple[2]);
                    return target;
                });
        nativeQuery.setParameter("pageSize", page.getPageSize());
        nativeQuery.setParameter("offset", page.getOffset());
        // set unionQuery
        List<Union> res = nativeQuery.getResultList();
        return new ReckonerUnionQuery(res, total);
    }

}
