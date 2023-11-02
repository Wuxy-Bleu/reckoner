package demo.usul.config;

import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

public class String2UUIDConverter implements Converter<String, UUID> {

    @Override
    public UUID convert(String event) {
        return UUID.fromString(event);
    }
}
