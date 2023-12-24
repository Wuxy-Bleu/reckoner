package demo.usul.config;

import demo.usul.LocaleConst;
import demo.usul.interceptor.AbstractInterceptor;
import demo.usul.mvc.RequestContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Slf4j
public class LocaleInterceptor extends AbstractInterceptor {

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info(request.getRequestURI());
        Locale lang = LocaleConst.DEFAULT;
        if (StringUtils.hasText(request.getHeader("Accept-Language"))) {
            lang = Locale.LanguageRange.parse(request.getHeader("Accept-Language"))
                    .stream()
                    .map(range -> new Locale(range.getRange()))
                    .toList()
                    .get(0);
        }
        RequestContextUtils.setAttrsLang(lang);
        return super.preHandle(request, response, handler);
    }

    @Override
    public List<String> addPathPatterns() {
        return List.of("/v1/accounts/**");
    }
}
