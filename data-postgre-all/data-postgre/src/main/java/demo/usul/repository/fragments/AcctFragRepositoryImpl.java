package demo.usul.repository.fragments;

import demo.usul.convert.AccountMapper;
import demo.usul.dto.AccountUpdateDto;
import demo.usul.entity.AccountEntity;
import demo.usul.entity.CardTypeEntity;
import demo.usul.exception.PostgreDeleteException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static demo.usul.entity.AccountEntity.IF_ENTITIES_EXIST;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class AcctFragRepositoryImpl implements AcctFragRepository {

    private final AccountMapper accountMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveWithAssociations(AccountEntity accountEntity) {
        TypedQuery<CardTypeEntity> query = entityManager
                .createQuery("select c from CardTypeEntity c where c.typeName = :typeName", CardTypeEntity.class)
                .setParameter("typeName", accountEntity.getTypeName());
        CardTypeEntity cardTypeEntity = query.getSingleResult();
        accountEntity.setCardTypeEntity(cardTypeEntity);
        entityManager.persist(accountEntity);
    }

    public List<AccountEntity> updateBatch(final Map<UUID, AccountUpdateDto> toUpdate, Set<UUID> ids) {
        throwWhenEntitiesNotExist(ids);
        TypedQuery<AccountEntity> acctFindByIds = entityManager.createNamedQuery(AccountEntity.FIND_BY_IDS, AccountEntity.class);
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
        throwWhenEntitiesNotExist(ids);
        return entityManager.createNamedQuery(AccountEntity.SOFT_DELETE).setParameter("ids", ids).executeUpdate();
    }

    public int updateBalBatch(Map<UUID, BigDecimal> blcs) {
        StringBuilder updateStrBuilder = new StringBuilder("update ").append(AccountEntity.class.getSimpleName()).append(" set ").append(AccountEntity.COLUMN_BALANCE_NAME).append(" = case ");
        for (int i = 0; i < blcs.size(); i++) {
            updateStrBuilder.append("when id = :id").append(i).append(" then :value").append(i);
        }
        updateStrBuilder.append(" end where id in (:ids)");

        Query query = entityManager.createQuery(updateStrBuilder.toString());
        int i = 0;
        for (Map.Entry<UUID, BigDecimal> entry : blcs.entrySet()) {
            query.setParameter("id" + i, entry.getKey());
            query.setParameter("value" + i, entry.getValue());
            i++;
        }
        query.setParameter("ids", blcs.keySet());
        return query.executeUpdate();
    }

    // 可以放到service层，用缓存解决
    public void throwWhenEntitiesNotExist(final Collection<UUID> ids) {
        List<AccountEntity> deleted = entityManager
                .createNamedQuery(IF_ENTITIES_EXIST, AccountEntity.class)
                .setParameter("ids", ids)
                .getResultList();
        if (!CollectionUtils.isEmpty(deleted))
            throw new PostgreDeleteException(
                    String.join(",",
                            deleted.stream().map(AccountEntity::getName).toList())
                            + " not exist");

    }
}
