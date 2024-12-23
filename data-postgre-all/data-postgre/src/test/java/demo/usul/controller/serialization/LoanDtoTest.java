package demo.usul.controller.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.usul.dto.LoanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoanDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testDatetimeFormatWithZone() {
        LoanDto dto = new LoanDto();
        dto.setTransDate(OffsetDateTime.now());
        Map<?, ?> map = objectMapper.convertValue(dto, Map.class);
        String transDate = (String) map.get("trans_date");
        assertThat(transDate).matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\+08:00$");
    }
}
