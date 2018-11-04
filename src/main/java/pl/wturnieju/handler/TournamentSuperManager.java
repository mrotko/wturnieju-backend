package pl.wturnieju.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.service.ITournamentService;

@Component
@RequiredArgsConstructor
public class TournamentSuperManager implements InitializingBean {

    private final ITournamentService tournamentService;

    private Map<String, ITournamentSystem> tournamentIdToTournamentSystemMap = new HashMap<>();

    private void loadActiveTournaments() {
        tournamentService.getAllActive().forEach(tournament ->
                tournamentIdToTournamentSystemMap
                        .put(tournament.getId(), TournamentSystemFactory.getInstance(tournament)));
    }

    @Override
    public void afterPropertiesSet() {
        loadActiveTournaments();
    }
}
