package demo.usul.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Slf4j
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modulesToInstall(new JavaTimeModule());
    }

    // for convert dto 2 csv string
    @Bean
    public CsvMapper csvMapper() {
        return (CsvMapper) CsvMapper.builder()
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .build()
                .findAndRegisterModules();
    }

    // 某些需要自动注入objectMapper的地方会错误注入csvMapper，只能注册一个default ObjectMapper
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
