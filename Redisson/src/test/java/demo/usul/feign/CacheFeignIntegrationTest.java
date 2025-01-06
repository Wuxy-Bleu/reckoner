package demo.usul.feign;

import demo.usul.config.TestConfig;
import demo.usul.dto.AccountDto;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
@ActiveProfiles("test")
class CacheFeignIntegrationTest {

    @Autowired
    private CacheFeign cacheFeign;

    @Test
    void whenValidInput_thenSuccess() {
        // Given
        AccountDto validAccount = new AccountDto();
        validAccount.setId("123");
        validAccount.setName("Test Account");

        // When & Then
        Object result = cacheFeign.cacheAccounts(List.of(validAccount), 1000L);
        assertThat(result).isNotNull();
    }

    @Test
    void whenInvalidInput_thenValidationError() {
        // Given
        AccountDto invalidAccount = new AccountDto();
        // id is null, should trigger validation

        // When & Then
        assertThrows(ConstraintViolationException.class, () -> 
            cacheFeign.cacheAccounts(List.of(invalidAccount), 1000L)
        );
    }

    @Test
    void whenOptionalParams_thenHandledCorrectly() {
        // Test with all params null
        var result1 = cacheFeign.getCachedAccounts(null, null, null);
        assertThat(result1).isNotNull();

        // Test with some params
        var result2 = cacheFeign.getCachedAccounts("testName", null, "USD");
        assertThat(result2).isNotNull();
    }

    @Test
    void whenEmptyList_thenHandledCorrectly() {
        Object result = cacheFeign.cacheAccounts(List.of(), 1000L);
        assertThat(result).isNotNull();
    }

    @Test
    void whenNullList_thenValidationError() {
        assertThrows(ConstraintViolationException.class, () -> 
            cacheFeign.cacheAccounts(null, 1000L)
        );
    }

    @Test
    void whenInvalidMs_thenError() {
        AccountDto validAccount = new AccountDto();
        validAccount.setId("123");
        validAccount.setName("Test Account");

        assertThrows(IllegalArgumentException.class, () -> 
            cacheFeign.cacheAccounts(List.of(validAccount), -1L)
        );
    }
} 