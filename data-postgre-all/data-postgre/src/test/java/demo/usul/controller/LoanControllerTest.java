package demo.usul.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoanControllerTest {

    private static final String URL = "http://127.0.0.1:8080/v1/loan";
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeAll() {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter(objectMapper));
    }

    void testGetAllLoans() {
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, null, String.class);
        String body = response.getBody();
        assertThat(body).isNotBlank();

    }

}