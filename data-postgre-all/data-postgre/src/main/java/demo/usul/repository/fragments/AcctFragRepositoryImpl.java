package demo.usul.repository.fragments;

import demo.usul.convert.AccountMapper;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.CardTypeEntity;
import demo.usul.feign.dto.AccountUpdateDto;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class AcctFragRepositoryImpl implements AcctFragRepository {

    private final AccountMapper accountMapper;

    @Resource
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager entityManager;

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

    public List<AccountEntity> updateBatch(final Map<UUID, AccountUpdateDto> toUpdate, Set<UUID> ids) {
        TypedQuery<AccountEntity> acctFindByIds = entityManager.createNamedQuery("acct_findByIds", AccountEntity.class);
        List<AccountEntity> fetched = acctFindByIds.setParameter("ids", ids).getResultList();
        fetched.forEach(
                e -> {
                    AccountUpdateDto newOne = toUpdate.get(e.getId());
                    accountMapper.updateAccountEntityFromAccountUpdateDto(newOne, e);
                }
        );
        return fetched;
    }

    public int softDelete(final List<UUID> ids) {
        return entityManager.createNamedQuery("soft_delete").setParameter("ids", ids).executeUpdate();
    }
}
