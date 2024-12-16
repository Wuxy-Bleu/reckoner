package demo.usul.repository.fragments;

import java.util.List;
import java.util.Optional;

public interface RcknTypeFragRepository {

    List<RcknTypeFragRepositoryImpl.Stat> stat(Optional<Boolean> monthly, Optional<Boolean> weekly);

    List<RcknTypeFragRepositoryImpl.Stat> statWithType(String type, Optional<Boolean> isOrderByNumber, Optional<Short> inOut, Optional<Boolean> isCurrentMonth, Optional<Boolean> monthly, Optional<Boolean> weekly);
}
