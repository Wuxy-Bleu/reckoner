package demo.usul.repository.fragments;

import java.util.List;
import java.util.Optional;

public interface RcknTypeFragRepository {

    List<RcknTypeFragRepositoryImpl.Stat> stat(Optional<Boolean> monthly);
}
