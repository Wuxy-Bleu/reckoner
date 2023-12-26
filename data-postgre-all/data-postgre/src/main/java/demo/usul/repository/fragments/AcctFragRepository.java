package demo.usul.repository.fragments;

import demo.usul.dto.AccountUpdateDto;
import demo.usul.entity.AccountEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface AcctFragRepository {

    void saveAssociations(AccountEntity entity);

    List<AccountEntity> updateBatch(final Map<UUID, AccountUpdateDto> toUpdate, Set<UUID> ids);

    int softDelete(final List<UUID> ids);
}
