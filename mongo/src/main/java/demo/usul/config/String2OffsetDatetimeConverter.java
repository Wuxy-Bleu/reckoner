package demo.usul.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.OffsetDateTime;

@ReadingConverter
public class String2OffsetDatetimeConverter implements Converter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(String source) {
        return OffsetDateTime.parse(source);
    }
}
