//package demo.usul.repository;
//
//import demo.usul.entity.AccountEntity;
//import demo.usul.entity.ReckonerEntity;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//import org.springframework.data.jpa.repository.support.JpaEntityInformation;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//
//import java.util.Optional;
//import java.util.UUID;
//
//public class baseRepositoryImpl<T, ID>
//        extends SimpleJpaRepository<T, ID> {
//
//    private final EntityManager entityManager;
//
//    public baseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
//        super(entityInformation, entityManager);
//        this.entityManager = entityManager;
//    }
//
//    public void saveAssociations(T entity) {
//        Short inOut = entity.getInOut();
//        Optional<UUID> toAcct = Optional.ofNullable(entity.getToAcctEntity().getId());
//        Optional<UUID> fromAcct = Optional.ofNullable(entity.getFromAcctObj().getId());
//
//        EntityTransaction transaction = entityManager.getTransaction();
//        transaction.begin();
//
//        Optional<AccountEntity> toAcctEnt = toAcct.map(uuid -> entityManager.find(AccountEntity.class, uuid));
//        Optional<AccountEntity> fromAcctEnt = fromAcct.map(uuid -> entityManager.find(AccountEntity.class, uuid));
//        switch (inOut) {
//            case 0:
//                entity.setToAcctEntity(toAcctEnt.orElseThrow());
//                entity.setFromAcctObj(fromAcctEnt.orElseThrow());
//                break;
//            case 1:
//                entity.setToAcctEntity(toAcctEnt.orElseThrow());
//                break;
//            case -1:
//                entity.setFromAcctObj(fromAcctEnt.orElseThrow());
//                break;
//            default:
//                break;
//        }
//
//        entityManager.persist(entity);
//        transaction.commit();
//        entityManager.close();
//    }
//}
