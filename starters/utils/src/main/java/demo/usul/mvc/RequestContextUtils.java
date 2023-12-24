package demo.usul.mvc;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Locale;
import java.util.Objects;

public class RequestContextUtils {

    private RequestContextUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Locale getAttrsLang() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        return (Locale) requestAttributes.getAttribute("lang", RequestAttributes.SCOPE_REQUEST);
    }

    public static void setAttrsLang(Locale locale) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        requestAttributes.setAttribute("lang", locale, RequestAttributes.SCOPE_REQUEST);
    }
}
