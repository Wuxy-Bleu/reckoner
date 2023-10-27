package demo.usul.repository.fragments;

import demo.usul.entity.AccountEntity;
import demo.usul.entity.CardTypeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FragmentRepositoryImpl implements FragmentRepository {

    private final EntityManagerFactory emf;

    @Override
    public void saveAssociations(AccountEntity accountEntity) {
        EntityManager cem = emf.createEntityManager();
        CardTypeEntity ct = accountEntity.getCardTypeEntity();
        try {
            cem.getTransaction().begin();
            TypedQuery<CardTypeEntity> query = cem.createQuery("select c from CardTypeEntity c where c.typeName = :typeName", CardTypeEntity.class).setParameter("typeName", accountEntity.getTypeName());
            ct = query.getSingleResult();
        } catch (NoResultException e) {
            log.info("xxxxx");
        }
        accountEntity.setCardTypeEntity(ct);
        cem.persist(accountEntity);
        cem.getTransaction().commit();
    }
}
