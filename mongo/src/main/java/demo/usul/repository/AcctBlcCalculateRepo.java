package demo.usul.repository;

import demo.usul.dto.AcctBlcCalculateDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface AcctBlcCalculateRepo extends MongoRepository<AcctBlcCalculateDto, String> {

    AcctBlcCalculateDto findByAfterRekn(UUID afterRekn);
}
