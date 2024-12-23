package demo.usul.controller.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.usul.dto.LoanCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class LoanCreateDtoTest2 {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testJsonFormat4OffsetDatetime() {
        String json = """
                    {
                        "principal": 199,
                        "from_acct": "0211a828-c470-46c5-b2f1-5fbebd9a192a",
                        "trans_date": "2024-12-23 01:34:24+08:00"
                    }
                """;
        assertThatCode(
                () -> objectMapper.readValue(json, LoanCreateDto.class))
                .doesNotThrowAnyException();
    }

    @Test
    void testJsonFormatWhenSerialize() {
        LoanCreateDto dto = new LoanCreateDto();
        dto.setTransDate(OffsetDateTime.now());
        Map<?, ?> map = objectMapper.convertValue(dto, Map.class);
        String transDate = (String) map.get("trans_date");
        assertThat(transDate).matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
    }
}