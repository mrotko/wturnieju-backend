package pl.wturnieju.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.log4j.Log4j2;

@Log4j2
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${path.server-url}")
    private String serverUrl;

    @Value("${path.frontend-url}")
    private String frontendUrl;

    @Value("${path.api-url}")
    private String apiUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.debug("Register CORS configuration");
        registry.addMapping("/**")
                .allowedOrigins(frontendUrl)
                .allowedMethods("PUT", "DELETE", "GET", "POST")
                .allowCredentials(true);
    }
}
