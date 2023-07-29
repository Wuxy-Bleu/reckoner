package demo.usul.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.OffsetDateTime;

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
                .create();
    }

}
