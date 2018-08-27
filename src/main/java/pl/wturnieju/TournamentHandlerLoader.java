package pl.wturnieju;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.exception.InvalidCompetitionException;
import pl.wturnieju.exception.InvalidTournamentSystemException;
import pl.wturnieju.model.generic.Tournament;

@Component
@RequiredArgsConstructor
public class TournamentHandlerLoader {

    private final TournamentStateRepository stateRepository;

    public Optional<ITournamentHandler> loadHandler(Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            TournamentHandler<SwissState> handler = TournamentHandlerFactory.<SwissState>getInstance(tournament)
                    .orElseThrow(InvalidCompetitionException::new);
            handler.setStateRepository(stateRepository);
            TournamentState state = loadTournamentState(tournament);

            handler.setTournamentState((SwissState) state);

            TournamentSystem system = TournamentSystemFactory.getInstance(tournament.getSystemType())
                    .orElseThrow(InvalidTournamentSystemException::new);
            handler.setTournamentSystem(system);

            return Optional.of(handler);
        default:
            return Optional.empty();
        }
    }

    private TournamentState loadTournamentState(Tournament tournament) {
        return stateRepository.getByTournamentId(tournament.getId()).orElseGet(
                () -> TournamentStateFactory.getInstance(tournament)
                        .orElseThrow(InvalidTournamentSystemException::new));
    }

}
