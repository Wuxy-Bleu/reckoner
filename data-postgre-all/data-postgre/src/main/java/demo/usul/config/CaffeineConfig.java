//package demo.usul.config;
//
//import com.github.benmanes.caffeine.cache.Caffeine;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//public class CaffeineConfig {
//
//    @Bean
//    public Caffeine<?, ?> caffeineCfg() {
//        return Caffeine.newBuilder().maximumSize(10_000)
//                .recordStats().expireAfterWrite(60, TimeUnit.MINUTES);
//    }
//}
