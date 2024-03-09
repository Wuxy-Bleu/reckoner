package demo.usul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.usul.dto.AccountUpdateDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SerializeTest {

    final AccountUpdateDto accountUpdateDto = AccountUpdateDto.builder().name("random name").build();
    private AccountUpdateDto aud = AccountUpdateDto.builder().name("ajiowfjaie").build();
    private List<AccountUpdateDto> list = List.of(accountUpdateDto, aud);

    @Test
    void testSerializedByteArrSize_withDiffSerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writeValueAsBytes(accountUpdateDto);
        byte[] s = objectMapper.writeValueAsString(accountUpdateDto).getBytes(StandardCharsets.UTF_8);
        assertThat(bytes.length).isEqualTo(s.length);

        byte[] bytes1 = objectMapper.writeValueAsString(list).getBytes(StandardCharsets.UTF_8);
        byte[] bytes2 = objectMapper.writeValueAsBytes(list);
        assertThat(bytes1.length).isEqualTo(bytes2.length);
    }

    @Test
    void testDeserializePojoFromJsonStringBytes() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writeValueAsString(accountUpdateDto).getBytes(StandardCharsets.UTF_8);

        AccountUpdateDto accountUpdateDto1 = objectMapper.readValue(bytes, AccountUpdateDto.class);
        assertThat(accountUpdateDto1).isEqualTo(accountUpdateDto);

        byte[] bytes1 = objectMapper.writeValueAsString(list).getBytes(StandardCharsets.UTF_8);
        List<?> list1 = objectMapper.readValue(bytes1,
                new TypeReference<List<AccountUpdateDto>>() {});
        assertThat(list1.get(0)).isInstanceOf(AccountUpdateDto.class);

    }
}
