package pl.wturnieju.handler;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.TournamentStateFactory;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentState;
import pl.wturnieju.model.swiss.SwissState;
import pl.wturnieju.repository.TournamentStateRepository;

@Component
@RequiredArgsConstructor
public class TournamentHandlerLoader {

    private final TournamentStateRepository stateRepository;

    public ITournamentHandler loadHandler(Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            TournamentHandler<SwissState> handler = TournamentHandlerFactory.getInstance(tournament);
            handler.setStateRepository(stateRepository);
            TournamentState state = loadTournamentState(tournament);

            handler.setTournamentState((SwissState) state);

            TournamentSystem system = TournamentSystemFactory.getInstance(tournament.getSystemType());
            handler.setTournamentSystem(system);

            return handler;
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private TournamentState loadTournamentState(Tournament tournament) {
        return stateRepository.getByTournamentId(tournament.getId()).orElseGet(
                () -> TournamentStateFactory.getInstance(tournament));
    }
}
