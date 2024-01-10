package demo.usul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    public static final String DATABASE_NAME = "reckoner";

    @Override
    protected void configureConverters(MongoCustomConversions.MongoConverterConfigurationAdapter converterConfigurationAdapter) {
        converterConfigurationAdapter.registerConverter(new OffsetDateTime2StringConverter());
    }

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }
}
