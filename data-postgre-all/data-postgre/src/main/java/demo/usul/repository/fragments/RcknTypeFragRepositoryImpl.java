package demo.usul.repository.fragments;

import cn.hutool.core.util.ArrayUtil;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import demo.usul.entity.QReckonerEntity;
import demo.usul.entity.QReckonerTypeEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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

    @Override
    public List<Stat> stat(Optional<Boolean> monthly) {
        Expression<?>[] arr = new Expression<?>[]{reckonerType.typeName, reckoner.inOut};

        if (monthly.isPresent() && Boolean.TRUE.equals(monthly.get()))
            arr = ArrayUtil.append(arr, reckoner.transDate.month());

        return jpaQueryFactory.from(reckonerType)
                .innerJoin(reckonerType.reckonerObjs, reckoner)
                .groupBy(arr)
                .select(Projections.constructor(
                        Stat.class,
                        reckonerType.typeName,
                        monthly.orElse(false) ? reckoner.transDate.month() : Expressions.constant(0),
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

    public record Stat(String name, Integer month, short inOut, BigDecimal amount, Long num, OffsetDateTime minDate,
                       OffsetDateTime maxDate, BigDecimal min, BigDecimal max) {}
}

