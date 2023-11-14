package demo.usul.repository.fragments;

import demo.usul.entity.AccountEntity;
import org.springframework.data.repository.NoRepositoryBean;

public interface AcctFragRepository {

    void saveAssociations(AccountEntity entity);
}
