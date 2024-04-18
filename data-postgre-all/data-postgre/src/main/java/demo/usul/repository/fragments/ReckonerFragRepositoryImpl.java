package demo.usul.repository.fragments;

import demo.usul.entity.ReckonerEntity;
import demo.usul.entity.ReckonerTypeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class ReckonerFragRepositoryImpl implements ReckonerFragRepository {

    @PersistenceContext
    private EntityManager em;

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
}
