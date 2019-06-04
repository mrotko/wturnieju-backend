package pl.wturnieju.config.tournament.scoring;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import pl.wturnieju.config.AbstractConfigurationLoader;

@Configuration
@Getter
public class ScoringConfiguration extends AbstractConfigurationLoader {

    private List<Competition> competitions;

    public ScoringConfiguration(ApplicationContext context) {
        super(context);
    }

    @Override
    protected String getConfigFilename() {
        return "scoring.json";
    }

    @Override
    protected void readData(InputStream is) throws IOException {
        var objectMapper = new ObjectMapper();
        competitions = objectMapper.readValue(is, new TypeReference<List<Competition>>() {
        });
    }
}
