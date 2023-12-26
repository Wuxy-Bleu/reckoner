package demo.usul.repository;

import demo.usul.entity.AccountModifyRecord;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AccountModifyRepository extends MongoRepository<AccountModifyRecord, String> {
}
