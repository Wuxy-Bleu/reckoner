package demo.usul.repository.fragments;

import cn.hutool.core.util.ArrayUtil;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import demo.usul.entity.QReckonerEntity;
import demo.usul.entity.QReckonerTypeEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class RcknTypeFragRepositoryImpl implements RcknTypeFragRepository {

    protected final QReckonerTypeEntity reckonerType = QReckonerTypeEntity.reckonerTypeEntity;
    protected final QReckonerEntity reckoner = QReckonerEntity.reckonerEntity;
    @PersistenceContext
    private EntityManager em;
    private JPAQueryFactory jpaQueryFactory;

    @PostConstruct
    private void postConstruct() {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    /**
     * @param monthly if group by month
     * @param weekly  if group by week, month takes precedence over week
     * @return stats data
     */
    @Override
    public List<Stat> stat(Optional<Boolean> monthly, Optional<Boolean> weekly) {
        Expression<?>[] arr = new Expression<?>[]{reckonerType.typeName, reckoner.inOut};

        Expression<?>[] newArr = arr;
        boolean isMonthly = false;
        boolean isWeekly = false;
        if (weekly.isPresent() && Boolean.TRUE.equals(weekly.get())) {
            newArr = ArrayUtil.append(arr, reckoner.transDate.week());
            isWeekly = true;
        }
        if (monthly.isPresent() && Boolean.TRUE.equals(monthly.get())) {
            newArr = ArrayUtil.append(arr, reckoner.transDate.month());
            isMonthly = true;
            isWeekly = false;
        }

        return jpaQueryFactory.from(reckonerType)
                .innerJoin(reckonerType.reckonerObjs, reckoner)
                .groupBy(newArr)
                .select(Projections.constructor(
                        Stat.class,
                        reckonerType.typeName,
                        isMonthly ? reckoner.transDate.month() : Expressions.constant(0),
                        isWeekly ? reckoner.transDate.week() : Expressions.constant(0),
                        reckoner.inOut,
                        reckoner.amount.sum(),
                        reckoner.count(),
                        reckoner.transDate.min(),
                        reckoner.transDate.max(),
                        reckoner.amount.min(),
                        reckoner.amount.max()
                ))
                .fetch();
    }

    @Override
    public List<Stat> statWithType(String type, Optional<Boolean> isOrderByNumber, Optional<Short> inOut, Optional<Boolean> isCurrentMonth, Optional<Boolean> monthly, Optional<Boolean> weekly) {
        int monthValue = LocalDateTime.now(ZoneId.of("Asia/Shanghai")).getMonthValue();
        Short flow = inOut.orElse((short) -1);

        Predicate[] predicates = new Predicate[]{
                reckonerType.typeName.eq(type),
                reckoner.inOut.eq(flow)};
        if (isCurrentMonth.orElse(false))
            predicates = ArrayUtil.append(predicates, reckoner.transDate.month().eq(monthValue));


        Expression<?>[] arr = null;
        StringPath accName = flow == -1 ? reckoner.fromAcctObj.name : reckoner.toAcctObj.name;
        arr = ArrayUtil.append(arr, accName);

        Expression<?>[] newArr = arr;
        boolean isMonthly = false;
        boolean isWeekly = false;
        if (weekly.isPresent() && Boolean.TRUE.equals(weekly.get())) {
            newArr = ArrayUtil.append(arr, reckoner.transDate.week());
            isWeekly = true;
        }
        if (monthly.isPresent() && Boolean.TRUE.equals(monthly.get())) {
            newArr = ArrayUtil.append(arr, reckoner.transDate.month());
            isMonthly = true;
            isWeekly = false;
        }

        return jpaQueryFactory.from(reckonerType)
                .innerJoin(reckonerType.reckonerObjs, reckoner)
                .where(predicates)
                .groupBy(newArr)
                .select(Projections.constructor(
                        Stat.class,
                        accName,
                        isMonthly ? reckoner.transDate.month() : Expressions.constant(0),
                        isWeekly ? reckoner.transDate.week() : Expressions.constant(0),
                        Expressions.constant(flow),
                        reckoner.amount.sum(),
                        reckoner.id.count(),
                        reckoner.transDate.min(),
                        reckoner.transDate.max(),
                        reckoner.amount.min(),
                        reckoner.amount.max()
                ))
                .orderBy(isOrderByNumber.orElse(false) ? reckoner.id.count().desc() : reckoner.amount.sum().desc())
                .fetch();
    }

    public record Stat(String name,
                       Integer month,
                       Integer week,
                       short inOut,
                       BigDecimal amount,
                       Long num,
                       OffsetDateTime minDate,
                       OffsetDateTime maxDate,
                       BigDecimal min, BigDecimal max) {}
}

