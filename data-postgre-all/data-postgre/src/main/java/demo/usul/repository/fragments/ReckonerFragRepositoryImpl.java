package demo.usul.repository.fragments;

import com.querydsl.jpa.impl.JPAQuery;
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

import java.util.List;
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
    public List<ReckonerEntity> statsToAcc(UUID id) {
        JPAQuery<ReckonerEntity> query = jpaQueryFactory
                .from(reckoner)
                .select(reckoner)
                .innerJoin(reckoner.reckonerTypeObj, rcknType)
                .where(reckoner.toAcct.eq(id),
                        reckoner.isAlive.eq(true))
                .orderBy(reckoner.inOut.asc())
                .orderBy(reckoner.typeId.asc())
                .orderBy(reckoner.transDate.desc());
        return query.fetch();
    }
}
