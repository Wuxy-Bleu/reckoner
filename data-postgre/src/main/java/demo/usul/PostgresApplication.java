package demo.usul;

import com.google.gson.Gson;
import demo.usul.controller.ReckonerController;
import demo.usul.entity.AccountsEntity;
import demo.usul.entity.ReckonerEntity;
import demo.usul.repository.AccountsRepository;
import demo.usul.repository.CardTypeRepository;
import demo.usul.repository.ReckonerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class PostgresApplication {

    private AccountsRepository accountsRepository;

    private CardTypeRepository cardTypeRepository;

    private ReckonerRepository reckonerRepository;

    private ReckonerController reckonerController;

    private Gson gson;

    public static void main(String[] args) {
        SpringApplication.run(PostgresApplication.class);
    }

    @Bean
    public CommandLineRunner runner() {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                List<ReckonerEntity> reckoner = reckonerRepository.findAll();
                ReckonerEntity reckonerEntity = reckoner.get(0);
                AccountsEntity fromAcctEntity = reckonerEntity.getFromAcctEntity();
                log.info(fromAcctEntity.getName());
            }
        };
    }

}