package demo.usul.config;

import demo.usul.interceptor.AInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AInterceptor aInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the interceptor for all paths or specific patterns
        registry.addInterceptor(aInterceptor).addPathPatterns("/**");
    }
}
