package demo.usul.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.List;

public abstract class AbstractInterceptor implements HandlerInterceptor {

    public List<String> addPathPatterns() {
        return Collections.emptyList();
    }

    public List<String> excludePathPatterns() {
        return Collections.emptyList();
    }

}
