package demo.usul.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoanCreateDtoTest {

    private Validator validator;
    private ValidatorFactory factory;

    @BeforeEach
    void setUp() {
        // validator provided by hibernate
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void testNotNullAndAssertTrueAnnotation() {
        LoanCreateDto loanCreateDto = new LoanCreateDto();

        Set<ConstraintViolation<LoanCreateDto>> violations = validator.validate(loanCreateDto);

        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getMessage().equals("must not be null"))
                .anyMatch(v -> v.getMessage().equals("交易账户id或者名称至少有一个不为空"));
    }

}