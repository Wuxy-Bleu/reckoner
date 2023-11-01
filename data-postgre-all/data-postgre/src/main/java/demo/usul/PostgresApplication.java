package demo.usul;

import com.google.gson.Gson;
import demo.usul.controller.ReckonerController;
import demo.usul.entity.CardTypeEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.repository.AccountRepository;
import demo.usul.repository.CardTypeRepository;
import demo.usul.repository.ReckonerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
@EnableCaching
@ConfigurationPropertiesScan
//@EnableDiscoveryClient
//@EnableFeignClients
@EnableScheduling
@EnableAsync(mode = AdviceMode.ASPECTJ)
public class PostgresApplication {

    private AccountRepository accountRepository;

    private CardTypeRepository cardTypeRepository;

    private ReckonerRepository reckonerRepository;

    private ReckonerController reckonerController;

    private Gson gson;

    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(PostgresApplication.class);
    }

    @Bean
    public CommandLineRunner runner() {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) {
//                List<ReckonerEntity> reckoner = reckonerRepository.findAll();
//                ReckonerEntity reckonerEntity = reckoner.get(0);
//                AccountsEntity fromAcctEntity = reckonerEntity.getFromAcctEntity();
//                log.info(fromAcctEntity.getName());
//
//                AccountsEntity accountsEntity = accountsRepository.findByNameIgnoreCase("上海银行美团联名卡").get();
//                CardTypeEntity cardTypeEntity = accountsEntity.getCardTypeEntity();
//                List<ReckonerEntity> inReckonerEntities = accountsEntity.getInReckonerEntities();
//                ReckonerEntity outReckonerEntities = accountsEntity.getOutReckonerEntities().get(0);
//                log.info(outReckonerEntities.getId().toString());
            }
        };
    }
}