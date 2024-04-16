package demo.usul.repository.fragments;

import demo.usul.entity.AccountEntity;
import demo.usul.entity.ReckonerEntity;
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
        Optional<AccountEntity> toAcctOpt = Optional.ofNullable(entity.getToAcct())
                .map(uuid ->
                        em.createNamedQuery(AccountEntity.FIND_BY_IDS, AccountEntity.class)
                                .setParameter("ids", List.of(uuid)).getSingleResult());
        toAcctOpt = toAcctOpt.or(() ->
                Optional.ofNullable(entity.getFromAcctObj().getName())
                        .map(s ->
                                em.createNamedQuery(AccountEntity.FIND_BY_NAME, AccountEntity.class)
                                        .setParameter("name", s).getSingleResult()));

        Optional<AccountEntity> fromAcctOpt = Optional.ofNullable(entity.getFromAcct())
                .map(uuid ->
                        em.createNamedQuery(AccountEntity.FIND_BY_IDS, AccountEntity.class)
                                .setParameter("ids", List.of(uuid)).getSingleResult());
        fromAcctOpt = fromAcctOpt.or(() ->
                Optional.ofNullable(entity.getFromAcctObj().getName())
                        .map(s ->
                                em.createNamedQuery(AccountEntity.FIND_BY_NAME, AccountEntity.class)
                                        .setParameter("name", s).getSingleResult()));

        switch (entity.getInOut()) {
            case 0:
                entity.setToAcct(toAcctOpt.orElseThrow().getId());
                entity.setFromAcct(fromAcctOpt.orElseThrow().getId());
                break;
            case 1:
                entity.setToAcct(toAcctOpt.orElseThrow().getId());
                break;
            case -1:
                entity.setFromAcct(fromAcctOpt.orElseThrow().getId());
                break;
            default:
                break;
        }
        em.persist(entity);
    }
}
