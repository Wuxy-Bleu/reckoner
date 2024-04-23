package demo.usul.repository.fragments;

import demo.usul.dto.AccountUpdateDto;
import demo.usul.entity.AccountEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface AcctFragRepository {

    void saveWithAssociations(AccountEntity entity);

    List<AccountEntity> updateBatch(final Map<UUID, AccountUpdateDto> toUpdate, Set<UUID> ids);

    int softDelete(final List<UUID> ids);

    int updateBalBatch(Map<UUID, BigDecimal> blcs);
}
