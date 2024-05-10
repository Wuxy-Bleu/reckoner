package demo.usul.repository.fragments;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import demo.usul.entity.QReckonerEntity;
import demo.usul.entity.QReckonerTypeEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.entity.ReckonerTypeEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@Transactional
public class ReckonerFragRepositoryImpl implements ReckonerFragRepository {

    protected static final QReckonerEntity reckoner = QReckonerEntity.reckonerEntity;
    protected static final QReckonerTypeEntity rcknType = QReckonerTypeEntity.reckonerTypeEntity;
    @PersistenceContext
    private EntityManager em;
    private JPAQueryFactory jpaQueryFactory;

    @PostConstruct
    private void postConstruct() {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void saveAssociations(ReckonerEntity entity) {
        Optional<ReckonerTypeEntity> reckTyeOpt = Optional.ofNullable(entity.getTypeId())
                .map(uuid ->
                        em.createNamedQuery(ReckonerTypeEntity.FIND_BY_ID, ReckonerTypeEntity.class)
                                .setParameter("id", List.of(uuid)).getSingleResult());
        reckTyeOpt = reckTyeOpt.or(() ->
                Optional.ofNullable(entity.getReckonerTypeObj())
                        .map(ReckonerTypeEntity::getTypeName)
                        .map(s ->
                                em.createNamedQuery(ReckonerTypeEntity.FIND_BY_NAME, ReckonerTypeEntity.class)
                                        .setParameter("name", s).getSingleResult()));

        entity.setTypeId(reckTyeOpt.orElseThrow().getId());
        entity.setReckonerTypeObj(reckTyeOpt.orElseThrow());
        em.persist(entity);
    }

    @Override
    public List<ReckonerEntity> findByFromAcctAndTagsOrderByTransDateDesc(UUID id, String s, String s1) {
        String query = "select * from reckoner r " +
                "where r.from_acct = \'" + id.toString() + "\'" +
                " and (CAST(r.tags AS VARCHAR) ~ \'" + s + "\' " +
                " or CAST(r.tags AS VARCHAR) ~ \'" + s1 + "\') " +
                " order by r.trans_date DESC";
        return em.createNativeQuery(query, ReckonerEntity.class).getResultList();
    }

    @Override
    public List<ReckonerEntity> findByTagsOrderByTransDateDesc(String s, String s1) {
        String query = "select * from reckoner r " +
                "where  (CAST(r.tags AS VARCHAR) ~ \'" + s + "\' " +
                " or CAST(r.tags AS VARCHAR) ~ \'" + s1 + "\') " +
                " order by r.trans_date DESC";
        return em.createNativeQuery(query, ReckonerEntity.class).getResultList();
    }

    @Override
    public Map<String, List<Stat>> statsToAcc(UUID id) {
        Map<String, List<Stat>> res = new HashMap<>();
        res.put("all", statsInflowAggregationWhereToAcc(id));
        res.put("group_by_type", statsInflowAggregationWhereToAccGroupByTypeName(id));
        return res;
    }

    @Override
    public Map<String, List<Stat>> statsFromAcc(UUID id) {
        Map<String, List<Stat>> res = new HashMap<>();
        res.put("all", statsOutflowAggregationWhereToAcc(id));
        res.put("group_by_type", statsOutflowAggregationWhereToAccGroupByTypeName(id));
        return res;
    }

    private List<Stat> statsOutflowAggregationWhereToAcc(UUID fromAcc) {
        return jpaQueryFactory
                .from(reckoner)
                .innerJoin(reckoner.reckonerTypeObj, rcknType)
                .select(Projections.constructor(
                        Stat.class,
                        Expressions.constant("all_type"),
                        reckoner.count(),
                        reckoner.amount.sum(),
                        reckoner.amount.avg(),
                        reckoner.amount.min(),
                        reckoner.amount.max(),
                        reckoner.transDate.min(),
                        reckoner.transDate.max()
                ))
                .where(reckoner.fromAcct.eq(fromAcc),
                        reckoner.isAlive.eq(true),
                        reckoner.inOut.eq((short) -1))
                .fetch();
    }


    private List<Stat> statsInflowAggregationWhereToAcc(UUID toAcc) {
        return jpaQueryFactory
                .from(reckoner)
                .innerJoin(reckoner.reckonerTypeObj, rcknType)
                .select(Projections.constructor(
                        Stat.class,
                        Expressions.constant("all_type"),
                        reckoner.count(),
                        reckoner.amount.sum(),
                        reckoner.amount.avg(),
                        reckoner.amount.min(),
                        reckoner.amount.max(),
                        reckoner.transDate.min(),
                        reckoner.transDate.max()
                ))
                .where(reckoner.toAcct.eq(toAcc),
                        reckoner.isAlive.eq(true),
                        reckoner.inOut.eq((short) 1))
                .fetch();
    }

    private List<Stat> statsOutflowAggregationWhereToAccGroupByTypeName(UUID fromAcc) {
        return jpaQueryFactory
                .from(reckoner)
                .innerJoin(reckoner.reckonerTypeObj, rcknType)
                .select(Projections.constructor(
                        Stat.class,
                        reckoner.reckonerTypeObj.typeName,
                        reckoner.count(),
                        reckoner.amount.sum(),
                        reckoner.amount.avg(),
                        reckoner.amount.min(),
                        reckoner.amount.max(),
                        reckoner.transDate.min(),
                        reckoner.transDate.max()
                ))
                .where(reckoner.fromAcct.eq(fromAcc),
                        reckoner.isAlive.eq(true),
                        reckoner.inOut.eq((short) -1))
                .groupBy(reckoner.reckonerTypeObj.typeName)
                .fetch();
    }

    private List<Stat> statsInflowAggregationWhereToAccGroupByTypeName(UUID toAcc) {
        return jpaQueryFactory
                .from(reckoner)
                .innerJoin(reckoner.reckonerTypeObj, rcknType)
                .select(Projections.constructor(
                        Stat.class,
                        reckoner.reckonerTypeObj.typeName,
                        reckoner.count(),
                        reckoner.amount.sum(),
                        reckoner.amount.avg(),
                        reckoner.amount.min(),
                        reckoner.amount.max(),
                        reckoner.transDate.min(),
                        reckoner.transDate.max()
                ))
                .where(reckoner.toAcct.eq(toAcc),
                        reckoner.isAlive.eq(true),
                        reckoner.inOut.eq((short) 1))
                .groupBy(reckoner.reckonerTypeObj.typeName)
                .fetch();
    }

    public record Stat(String reckonerType,
                       Long count, BigDecimal sum, Double avg,
                       BigDecimal min, BigDecimal max,
                       OffsetDateTime minDate,
                       OffsetDateTime maxDate) {}

}
