package pl.wturnieju.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.service.ITournamentService;

@Component
@RequiredArgsConstructor
public class TournamentSuperManager implements InitializingBean {

    private Map<String, ITournamentHandler> tournamentIdToHandler = new HashMap<>();

    private final ITournamentService ITournamentService;

    private final TournamentHandlerLoader handlerLoader;

    private void loadActiveTournaments() {
        List<Tournament> tournaments = ITournamentService.getAllActive();
        loadHandlers(tournaments);
    }

    private void loadHandlers(List<Tournament> tournaments) {
        tournaments.forEach(tournament ->
                tournamentIdToHandler.put(tournament.getId(), handlerLoader.loadHandler(tournament)));
    }

    @Override
    public void afterPropertiesSet() {
        loadActiveTournaments();
    }

    public Optional<ITournamentHandler> getTournamentHandler(String tournamentId) {
        return Optional.ofNullable(tournamentIdToHandler.getOrDefault(tournamentId, null));
    }

    public <T extends ITournamentHandler> Optional<T> getTournamentHandler(String tournamentId, Class<T> clazz) {
        Optional<ITournamentHandler> tournamentHandler = getTournamentHandler(tournamentId);

        if (!tournamentHandler.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(clazz.cast(tournamentHandler));
    }
}
