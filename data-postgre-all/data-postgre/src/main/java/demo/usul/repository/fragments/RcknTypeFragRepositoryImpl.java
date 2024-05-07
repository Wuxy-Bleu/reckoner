package demo.usul.repository.fragments;

import com.querydsl.core.types.Projections;
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
    public List<Stat> stat() {
        return jpaQueryFactory.from(reckonerType)
                .innerJoin(reckonerType.reckonerObjs, reckoner)
                .groupBy(reckonerType.typeName)
                .select(Projections.constructor(
                        Stat.class,
                        reckonerType.typeName,
                        reckoner.amount.sum(),
                        reckoner.count(),
                        reckoner.transDate.min(),
                        reckoner.transDate.max()
                ))
                .fetch();
    }

    public record Stat(String name, BigDecimal amount, Long num, OffsetDateTime min, OffsetDateTime max) {}
}

