package demo.usul;

import demo.usul.config.LocaleInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@EnableConfigurationProperties
@ConfigurationPropertiesScan
class PilotApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    // mock a http request, and check if an interceptor will be called
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Test
    void shouldEnableLocaleRegistryLocaleInterceptor() {
        assertDoesNotThrow(() -> applicationContext.getBean(LocaleInterceptor.class));
    }

    @Test
    void shouldLocaleInterceptorAppliedToAllPath() {

    }
}
