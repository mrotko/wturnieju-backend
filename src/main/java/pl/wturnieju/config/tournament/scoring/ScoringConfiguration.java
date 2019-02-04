package pl.wturnieju.config.tournament.scoring;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentSystemType;
import pl.wturnieju.tournament.GameResultType;

@Configuration
@Getter
@RequiredArgsConstructor
public class ScoringConfiguration {

    private List<Competition> competitions;

    private final ApplicationContext context;

    @PostConstruct
    private void init() {
        try {
            InputStream is = context.getResource("classpath:config/scoring.json").getInputStream();
            var objectMapper = new ObjectMapper();
            competitions = objectMapper.readValue(is, new TypeReference<List<Competition>>() {
            });
        } catch (IOException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public Map<GameResultType, Double> getResultPoints(CompetitionType competitionType,
            TournamentSystemType tournamentSystemType) {
        return Collections.emptyMap();
    }
}
