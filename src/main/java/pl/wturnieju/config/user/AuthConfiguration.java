package pl.wturnieju.config.user;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.config.AbstractConfigurationLoader;

@Getter
@Setter
@Configuration
public class AuthConfiguration extends AbstractConfigurationLoader {

    private AuthConfigurationData authConfigurationData;

    public AuthConfiguration(ApplicationContext context) {
        super(context);
    }

    @Override
    protected String getConfigFilename() {
        return "auth.json";
    }

    @Override
    protected void readData(InputStream is) throws IOException {
        var objectMapper = new ObjectMapper();
        authConfigurationData = objectMapper.readValue(is, AuthConfigurationData.class);
    }
}
