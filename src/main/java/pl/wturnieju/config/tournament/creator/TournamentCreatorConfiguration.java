package pl.wturnieju.config.tournament.creator;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Configuration
@Getter
@RequiredArgsConstructor
public class TournamentCreatorConfiguration {

    private TournamentCreatorConfigurationData tournamentCreatorConfigurationData;

    private final ApplicationContext context;

    @PostConstruct
    private void init() {
        try {
            InputStream is = context.getResource("classpath:config/tournament-creator.json").getInputStream();
            var objectMapper = new ObjectMapper();
            tournamentCreatorConfigurationData = objectMapper.readValue(is,
                    new TypeReference<TournamentCreatorConfigurationData>() {
                    });
        } catch (IOException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
