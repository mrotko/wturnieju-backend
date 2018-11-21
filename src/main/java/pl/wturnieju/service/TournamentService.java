package pl.wturnieju.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.handler.TournamentSystem;
import pl.wturnieju.handler.TournamentSystemFactory;
import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.model.generic.GenericTournamentTable;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentSystemState;
import pl.wturnieju.repository.TournamentRepository;

@Service
@RequiredArgsConstructor
public class TournamentService implements ITournamentService {

    private final TournamentRepository tournamentRepository;

    @Override
    public void updateTournament(Tournament tournament) {
        if (tournament != null) {
            tournamentRepository.save(tournament);
        }
    }

    @Override
    public void updateTournament(GenericTournamentUpdateBundle bundle) {
        Tournament tournament = tournamentRepository.findById(bundle.getTournamentId()).orElseThrow();
        TournamentSystem system = TournamentSystemFactory.getInstance(tournament);
        system.updateTournament(bundle);
        tournamentRepository.save(tournament);
    }

    // TODO(mr): 21.11.2018 test
    @Override
    public void updateFixture(GenericFixtureUpdateBundle bundle) {
        Tournament tournament = tournamentRepository.findById(bundle.getTournamentId()).orElseThrow();
        TournamentSystem system = TournamentSystemFactory.getInstance(tournament);
        system.updateFixture(bundle);
        tournamentRepository.save(tournament);
    }

    @Override
    public Optional<Tournament> getById(String tournamentId) {
        return tournamentRepository.findById(tournamentId);
    }

    @Override
    public Map<TournamentStatus, List<Tournament>> getAllUserTournamentsGroupedByStatus(String userId) {
        // TODO(mr): 16.11.2018  to test
        return tournamentRepository.findAll().stream()
                // TODO(mr): 16.11.2018 or contributor
                .filter(tournament -> tournament.getOwner().getId().equals(userId))
                .collect(Collectors.groupingBy(Tournament::getStatus, Collectors.toList()));

    }

    // TODO(mr): 21.11.2018 test
    @Override
    public Fixture getFixtureById(String tournamentId, String fixtureId) {
        return tournamentRepository.findById(tournamentId)
                .map(Tournament::getTournamentSystemState)
                .flatMap(state -> state.getFixtureById(fixtureId))
                .orElse(null);
    }

    @Override
    public GenericTournamentTable getTournamentTable(String tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(Tournament::getTournamentSystemState)
                .map(TournamentSystemState::getTournamentTable)
                .orElse(null);
    }


}
