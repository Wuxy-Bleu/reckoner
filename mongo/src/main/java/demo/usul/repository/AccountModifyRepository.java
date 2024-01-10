package demo.usul.repository;

import demo.usul.entity.AccountModifyRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface AccountModifyRepository extends MongoRepository<AccountModifyRecord, String> {

    List<AccountModifyRecord> findByUuidAllIgnoreCase(String uuid);
}
