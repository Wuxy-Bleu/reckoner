package demo.usul.repository.fragments;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import demo.usul.dto.TransactionQueryCriteria;
import demo.usul.entity.LoanEntity;
import demo.usul.entity.QLoanEntity;
import demo.usul.entity.ReckonerUnionQuery;
import demo.usul.entity.ReckonerUnionQuery.Union;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.Query;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static demo.usul.Const.SHANG_HAI;

@SuppressWarnings("unchecked")
public class LoanFragRepositoryImpl implements LoanFragRepository {

    protected static final QLoanEntity loan = QLoanEntity.loanEntity;
    @PersistenceContext
    private EntityManager em;
    private JPAQueryFactory jpaQueryFactory;

    @PostConstruct
    private void postConstruct() {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

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

    @Override
    public List<LoanEntity> findCriteria(TransactionQueryCriteria criteria) {
        return jpaQueryFactory.from(loan)
                .where(mapCriteria2Predicate(criteria))
                .select(loan)
                .orderBy(loan.transDate.desc())
                .fetch();

    }

    private Predicate[] mapCriteria2Predicate(TransactionQueryCriteria criteria) {
        List<Predicate> where = new ArrayList<>();
        if (criteria.getFromAcct() != null)
            where.add(loan.fromAcctEntity.id.eq(criteria.getFromAcct()));
        if (criteria.getFromAcctName() != null)
            where.add(loan.fromAcctEntity.name.eq(criteria.getFromAcctName()));
//        if (criteria.getTagsContains() != null)
//            where.add(Expressions.booleanTemplate("{0} @> {1}", loan.tags,
//                    "'[" + criteria.getTagsContains().stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(" ")) + "]'"));
        if (criteria.getTransMonth() != null)
            where.add(loan.transDate.month().eq(criteria.getTransMonth()));
        where.add(loan.status.notEqualsIgnoreCase("deleted"));

        return where.toArray(new Predicate[0]);
    }

}
