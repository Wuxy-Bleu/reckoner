package demo.usul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocaleConfig {

    @Bean
    public LocaleInterceptor localeInterceptor() {
        return new LocaleInterceptor();
    }
}
