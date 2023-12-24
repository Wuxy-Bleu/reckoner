package demo.usul.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "data-postgre")
public class ConfigProperties {

    private String greeting;
}
