package demo.usul.repository.fragments;

import demo.usul.entity.AccountEntity;
import demo.usul.entity.ReckonerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
public class ReckonerFragRepositoryImpl implements ReckonerFragRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveAssociations(ReckonerEntity entity) {
        Short inOut = entity.getInOut();
        Optional<UUID> toAcct = Optional.ofNullable(entity.getToAcctEntity().getId());
        Optional<UUID> fromAcct = Optional.ofNullable(entity.getFromAcctObj().getId());

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Optional<AccountEntity> toAcctEnt = toAcct.map(uuid -> em.find(AccountEntity.class, uuid));
        Optional<AccountEntity> fromAcctEnt = fromAcct.map(uuid -> em.find(AccountEntity.class, uuid));
        switch (inOut) {
            case 0:
                entity.setToAcctEntity(toAcctEnt.orElseThrow());
                entity.setFromAcctObj(fromAcctEnt.orElseThrow());
                break;
            case 1:
                entity.setToAcctEntity(toAcctEnt.orElseThrow());
                break;
            case -1:
                entity.setFromAcctObj(fromAcctEnt.orElseThrow());
                break;
            default:
                break;
        }

        em.persist(entity);
        transaction.commit();
        em.close();
    }
}
