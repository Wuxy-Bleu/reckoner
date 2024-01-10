package demo.usul.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.OffsetDateTime;

@WritingConverter
public class OffsetDateTime2StringConverter implements Converter<OffsetDateTime, String> {

    @Override
    public String convert(OffsetDateTime source) {
        return source.toString();
    }
}
