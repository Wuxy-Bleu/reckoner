package demo.usul.repository.fragments;

import demo.usul.entity.LoanEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class LoanFragRepositoryImpl implements LoanFragRepository {

    @PersistenceContext
    private EntityManager em;

    public void create(LoanEntity loanEntity) {
        return;
    }

}
