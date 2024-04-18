package demo.usul.repository;

import demo.usul.dto.AcctBlcCalculateDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AcctBlcCalculateRepo extends MongoRepository<AcctBlcCalculateDto, String> {
}
