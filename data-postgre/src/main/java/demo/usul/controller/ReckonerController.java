package demo.usul.controller;

import demo.usul.entity.AccountsEntity;
import demo.usul.entity.ReckonerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 问题已解决，这就用来测试事务传播吧
@RestController
@AllArgsConstructor
@Slf4j
public class ReckonerController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/test")
    @Transactional
    public ReckonerEntity test() {
        return entityManager.createQuery("select r from ReckonerEntity r", ReckonerEntity.class).getResultList().get(0);
    }

    @Transactional
    public void test2() {
        ReckonerEntity test = test();
        AccountsEntity fromAcctEntity = test.getFromAcctEntity();
        log.info(fromAcctEntity.getName());
    }
}
