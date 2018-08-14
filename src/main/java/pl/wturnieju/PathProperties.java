package pl.wturnieju;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "path")
@Getter
@Setter
@Validated
public class PathProperties {

    private String serverUrl;

    private String frontendUrl;

    private String apiUrl;

}
