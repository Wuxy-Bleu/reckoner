package demo.usul;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ApacheHttpClientTest {

    @Test
    void testCloseableHttpClient() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://localhost:9200/_cat/health?v=true&pretty");
            httpClient.execute(httpGet, response -> {
                Assertions.assertThat(response.getCode()).isEqualTo(HttpStatus.SC_OK);
                return response;
            });
        }
    }
}
