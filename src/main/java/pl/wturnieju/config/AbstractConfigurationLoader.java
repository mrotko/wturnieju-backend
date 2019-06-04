package pl.wturnieju.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public abstract class AbstractConfigurationLoader {

    private final ApplicationContext context;

    private Resource getResource() {
        return context.getResource("classpath:config/" + getConfigFilename());
    }

    @PostConstruct
    private void init() {
        try {
            readData(getResource().getInputStream());
            log.info("Configuration loaded: " + getConfigFilename());
        } catch (IOException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }

    protected abstract String getConfigFilename();

    protected abstract void readData(InputStream is) throws IOException;
}
