package demo.usul.repository;

import demo.usul.entity.ReckonerTypeEntity;
import demo.usul.repository.fragments.RcknTypeFragRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Transactional
public interface ReckonerTypeRepository extends JpaRepository<ReckonerTypeEntity, UUID>, RcknTypeFragRepository {

    List<ReckonerTypeEntity> deleteByTypeNameInIgnoreCase(@NonNull Collection<String> typeNames);
}