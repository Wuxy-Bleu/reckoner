package demo.usul.properties;

import demo.usul.mvc.RequestContextUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.core.ResolvableType.forClass;
import static org.springframework.core.ResolvableType.forClassWithGenerics;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public class I18nMsgProperties {

    private final Environment environment;

    public String getErrMsg(String errCode) {
        String lang = RequestContextUtils.getAttrsLang().getLanguage();

        @SuppressWarnings("unchecked") Map<String, Map<String, String>> map =
                (Map<String, Map<String, String>>) Binder.get(environment)
                        .bind(errCode, Bindable.of(
                                forClassWithGenerics(Map.class,
                                        forClass(String.class),
                                        forClassWithGenerics(Map.class, String.class, String.class))))
                        .get();
        return map.get(lang).get("message");

    }
}
