package pl.wturnieju.config.user;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Configuration
public class AuthConfiguration {

    private final ApplicationContext context;

    private AuthConfigurationData authConfigurationData;

    @PostConstruct
    private void init() {
        try {
            InputStream is = context.getResource("classpath:config/auth.json").getInputStream();
            var objectMapper = new ObjectMapper();
            authConfigurationData = objectMapper.readValue(is, AuthConfigurationData.class);
        } catch (IOException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
