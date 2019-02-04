package pl.wturnieju.config.tournament.creator;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Configuration
@Getter
public class TournamentCreatorConfiguration {

    private TournamentCreatorConfigurationData tournamentCreatorConfigurationData;

    @PostConstruct
    private void init() {
        try {
            var file = ResourceUtils.getFile("classpath*:config/tournament-creator.json");
            var objectMapper = new ObjectMapper();
            tournamentCreatorConfigurationData = objectMapper.readValue(file,
                    new TypeReference<TournamentCreatorConfigurationData>() {
                    });
        } catch (IOException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
