package demo.usul.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import demo.usul.anno.JpaExclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.OffsetDateTime;

// for hibernate serialize test, now useless
@Configuration
public class GsonConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(OffsetDateTime.class,
                        new TypeAdapter<OffsetDateTime>() {
                            @Override
                            public void write(JsonWriter out, OffsetDateTime value) throws IOException {
                                if (null == value)
                                    out.nullValue();
                                else
                                    out.value(value.toString());
                            }

                            @Override
                            public OffsetDateTime read(JsonReader in) {
                                return null;
                            }
                        })
                .addSerializationExclusionStrategy(GsonConfig.excludeWithAnnoJpaExclude())
                .create();
    }

    public static ExclusionStrategy excludeWithAnnoJpaExclude() {
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getAnnotation(JpaExclude.class) != null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
    }

}
