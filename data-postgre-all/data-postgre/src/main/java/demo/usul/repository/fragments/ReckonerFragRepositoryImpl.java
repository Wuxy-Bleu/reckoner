package demo.usul.repository.fragments;

import cn.hutool.core.util.ArrayUtil;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import demo.usul.entity.QAccountEntity;
import demo.usul.entity.QReckonerEntity;
import demo.usul.entity.QReckonerTypeEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.entity.ReckonerTypeEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static cn.hutool.core.collection.CollUtil.addAll;

@Slf4j
@Repository
@Transactional
public class ReckonerFragRepositoryImpl implements ReckonerFragRepository {

    protected static final QReckonerEntity reckoner = QReckonerEntity.reckonerEntity;
    protected static final QReckonerTypeEntity rcknType = QReckonerTypeEntity.reckonerTypeEntity;
    protected static final QAccountEntity account = QAccountEntity.accountEntity;
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

    @Override
    public List<ReckonerEntity> retrieveCondPageable(Optional<UUID> id, Optional<String> reckonerType, Optional<Integer> inOut, Optional<Boolean> isOrderByTransDate, Optional<Boolean> isOrderByAmount, Pageable page) {
        int flow = inOut.orElse(-1);
        Predicate[] where = ArrayUtil.append(null, reckoner.inOut.eq((short) flow));

        if (id.isPresent())
            where = ArrayUtil.append(where,
                    flow == -1 ? reckoner.fromAcctObj.id.eq(id.get()) : reckoner.toAcct.eq(id.get()));
        if (reckonerType.isPresent())
            where = ArrayUtil.append(where, rcknType.typeName.eq(reckonerType.get()));

        OrderSpecifier<?>[] order = ArrayUtil.append(null, isOrderByTransDate.orElse(true) ? reckoner.transDate.desc() : reckoner.amount.desc());

        return jpaQueryFactory
                .from(reckoner)
                .leftJoin(reckoner.reckonerTypeObj, rcknType)
                .where(where)
                .select(reckoner)
                .orderBy(order)
                .offset((long) page.getPageNumber() * page.getPageSize())
                .limit(page.getPageSize())
                .fetch();
    }

    @Override
    public Long countCond(Optional<UUID> id, Optional<String> reckonerType, Optional<Integer> inOut) {
        int flow = inOut.orElse(-1);
        Predicate[] where = ArrayUtil.append(null, reckoner.inOut.eq((short) flow));
        if (id.isPresent())
            where = ArrayUtil.append(where,
                    flow == -1 ? reckoner.fromAcctObj.id.eq(id.get()) : reckoner.toAcct.eq(id.get()));
        if (reckonerType.isPresent())
            where = ArrayUtil.append(where, rcknType.typeName.eq(reckonerType.get()));

        return jpaQueryFactory
                .from(reckoner)
                .leftJoin(reckoner.reckonerTypeObj, rcknType)
                .where(where)
                .select(reckoner.id.count())
                .fetchOne();
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
                .where(reckoner.fromAcctObj.id.eq(fromAcc),
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
                .where(reckoner.fromAcctObj.id.eq(fromAcc),
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

    @Override
    public Collection<AccStat> statsGroupByAcc(Optional<Boolean> isMonthly,
                                               Optional<Boolean> isWeekly,
                                               Optional<Boolean> isGroupByType,
                                               Optional<String> acc,
                                               Optional<OffsetDateTime> timeBegin,
                                               Optional<OffsetDateTime> timeEnd) {
        Expression<?>[] groupBy = new Expression<?>[]{reckoner.inOut, account.name};
        if (isMonthly.orElse(false))
            groupBy = ArrayUtil.append(groupBy, reckoner.transDate.month());
        if (isWeekly.orElse(false))
            groupBy = ArrayUtil.append(groupBy, reckoner.transDate.week());
        if (isGroupByType.orElse(false))
            groupBy = ArrayUtil.append(groupBy, rcknType.typeName);

        List<AccStat> q1 = jpaQueryFactory
                .from(reckoner)
                .leftJoin(reckoner.fromAcctObj, account)
                .on(reckoner.inOut.ne((short) 1))
                .leftJoin(reckoner.reckonerTypeObj, rcknType)
                .where(where((short) -1, acc, timeBegin, timeEnd))
                .orderBy(reckoner.inOut.asc(), reckoner.amount.sum().desc())
                .groupBy(groupBy)
                .select(select("from", isMonthly, isWeekly, isGroupByType))
                .fetch();
        List<AccStat> q2 = jpaQueryFactory
                .from(reckoner)
                .leftJoin(reckoner.toAcctObj, account)
                .on(reckoner.inOut.ne((short) -1))
                .leftJoin(reckoner.reckonerTypeObj, rcknType)
                .where(where((short) 1, acc, timeBegin, timeEnd))
                .orderBy(reckoner.inOut.asc(), reckoner.amount.sum().desc())
                .groupBy(groupBy)
                .select(select("to", isMonthly, isWeekly, isGroupByType))
                .fetch();
        return addAll(q1, q2);
    }

    @Override
    public ReckonerEntity persist(ReckonerEntity reckoner) {
        em.persist(reckoner);
        return reckoner;
    }

    private Predicate[] where(short fromTo, Optional<String> acc, Optional<OffsetDateTime> timeBegin, Optional<OffsetDateTime> timeEnd) {
        Predicate[] where = new Predicate[]{reckoner.inOut.ne((short) (fromTo * -1))};
        if (acc.isPresent())
            where = ArrayUtil.append(where, account.name.eq(acc.get()));
        if (timeBegin.isPresent() && timeEnd.isPresent())
            where = ArrayUtil.append(where, reckoner.transDate.between(timeBegin.get(), timeEnd.get()));
        return where;
    }

    private Expression<AccStat> select(String fromTo,
                                       Optional<Boolean> isMonthly,
                                       Optional<Boolean> isWeekly,
                                       Optional<Boolean> isGroupByType) {
        return Projections.constructor(
                AccStat.class,
                account.name,
                Expressions.constant(fromTo),
                reckoner.id.count(),
                reckoner.inOut,
                reckoner.amount.sum(),
                reckoner.amount.min(),
                reckoner.amount.max(),
                reckoner.transDate.min(),
                reckoner.transDate.max(),
                isMonthly.orElse(false) ? reckoner.transDate.month() : Expressions.constant(0),
                isWeekly.orElse(false) ? reckoner.transDate.week() : Expressions.constant(0),
                isGroupByType.orElse(false) ? rcknType.typeName : Expressions.constant(""),
                account.billingCycle.max(),
                account.dueDate.max()
        );
    }

    public record Stat(String reckonerType,
                       Long count, BigDecimal sum, Double avg,
                       BigDecimal min, BigDecimal max,
                       OffsetDateTime minDate,
                       OffsetDateTime maxDate) {}

    public record AccStat(String name, String fromTo, Long count, Short inOut, BigDecimal sum,
                          BigDecimal min, BigDecimal max,
                          OffsetDateTime minDate, OffsetDateTime maxDate,
                          Integer month, Integer week,
                          String type,
                          String billCycle,
                          String dueDate) {}
}
