package pl.wturnieju.config.tournament.scoring;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentSystemType;
import pl.wturnieju.tournament.GameResultType;

@Configuration
@Getter
public class ScoringConfiguration {

    private List<Competition> competitions;

    @PostConstruct
    private void init() {
        try {
            var file = ResourceUtils.getFile("classpath*:config/scoring.json");
            var objectMapper = new ObjectMapper();
            competitions = objectMapper.readValue(file, new TypeReference<List<Competition>>() {
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
