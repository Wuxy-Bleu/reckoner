package demo.usul;

import com.google.gson.Gson;
import demo.usul.entity.AccountsEntity;
import demo.usul.entity.CardTypeEntity;
import demo.usul.repository.AccountsRepository;
import demo.usul.repository.CardTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class PostgresApplication {

    private AccountsRepository accountsRepository;

    private CardTypeRepository cardTypeRepository;

    private Gson gson;

    public static void main(String[] args) {
        SpringApplication.run(PostgresApplication.class);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            List<AccountsEntity> all = accountsRepository.findAll();
            List<CardTypeEntity> all1 = cardTypeRepository.findAll();
            log.info(gson.toJson(all1));
            log.info(gson.toJson(all));
        };
    }
}