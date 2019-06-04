package pl.wturnieju.config.tournament.creator;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import pl.wturnieju.config.AbstractConfigurationLoader;

@Configuration
@Getter
public class TournamentCreatorConfiguration extends AbstractConfigurationLoader {

    private TournamentCreatorConfigurationData tournamentCreatorConfigurationData;

    public TournamentCreatorConfiguration(ApplicationContext context) {
        super(context);
    }

    @Override
    protected String getConfigFilename() {
        return "tournament-creator.json";
    }

    @Override
    protected void readData(InputStream is) throws IOException {
        var objectMapper = new ObjectMapper();
        tournamentCreatorConfigurationData = objectMapper.readValue(is, TournamentCreatorConfigurationData.class);
    }
}
