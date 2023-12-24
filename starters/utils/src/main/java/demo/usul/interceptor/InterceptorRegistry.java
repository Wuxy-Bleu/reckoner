package demo.usul.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class InterceptorRegistry implements WebMvcConfigurer {

    private final Map<String, AbstractInterceptor> beans;

    @Override
    public void addInterceptors(@SuppressWarnings("NullableProblems") org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        beans.values().forEach(
                e -> registry.addInterceptor(e)
                        .addPathPatterns(e.addPathPatterns())
                        .excludePathPatterns(e.excludePathPatterns())
        );
    }
}
