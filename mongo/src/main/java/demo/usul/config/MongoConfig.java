package demo.usul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    public static final String DATABASE_NAME = "reckoner";

    @Override
    protected void configureConverters(MongoCustomConversions.MongoConverterConfigurationAdapter converterConfigurationAdapter) {
        converterConfigurationAdapter.registerConverter(new OffsetDateTime2StringConverter());
        converterConfigurationAdapter.registerConverter(new String2OffsetDatetimeConverter());
    }

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
