package demo.usul.repository;

import demo.usul.dto.AccountUpdateDto;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AccountUpdateRepository extends CrudRepository<AccountUpdateDto, UUID> {
}
