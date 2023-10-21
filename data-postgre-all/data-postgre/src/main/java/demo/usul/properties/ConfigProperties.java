package demo.usul.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "data-postgre")
@Getter
@Setter
public class ConfigProperties {

    private String greeting;
}
