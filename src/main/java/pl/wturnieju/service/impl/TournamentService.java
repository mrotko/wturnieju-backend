package pl.wturnieju.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.TournamentSystemFactory;
import pl.wturnieju.validator.Validators;

@Service
@RequiredArgsConstructor
public class TournamentService implements ITournamentService {

    private final TournamentRepository tournamentRepository;

    private final ApplicationContext context;

    @Override
    public List<Tournament> getUserTournaments(String userId) {
        IProfile profile = () -> userId;
        return tournamentRepository.getAllByOwnerOrParticipants(profile, Collections.singletonList(profile));
    }

    @Override
    public void startTournament(String tournamentId) {
        var tournament = findTournament(tournamentId).orElseThrow(ResourceNotFoundException::new);
        validateTournamentParticipants(tournament);
        var system = TournamentSystemFactory.create(context, tournament);
        system.startTournament();
        tournamentRepository.save(tournament);
    }

    @Override
    public void finishTournament(String tournamentId) {
        var tournament = findTournament(tournamentId).orElseThrow(ResourceNotFoundException::new);
        var system = TournamentSystemFactory.create(context, tournament);
        system.finishTournament();
        tournamentRepository.save(tournament);
    }

    private void validateTournamentParticipants(Tournament tournament) {
        var validator = Validators.getTournamentParticipantsValidator();
        try {
            validator.validateAndThrowInvalid(tournament);
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public Tournament getTournament(String tournamentId) {
        return tournamentRepository.getById(tournamentId);
    }

    @Override
    public Optional<Tournament> findTournament(String tournamentId) {
        return tournamentRepository.findById(tournamentId);
    }

    @Override
    public void deleteTournament(String tournamentId) {
        tournamentRepository.deleteById(tournamentId);
    }

    @Override
    public void updateTournament(Tournament tournament) {
        if (tournament != null) {
            tournamentRepository.save(tournament);
        }
    }


    //    // TODO(mr): 21.11.2018 test
    //    @Override
    //    public void updateFixture(GenericFixtureUpdateBundle bundle) {
    //        var tournament = tournamentRepository.findUserById(bundle.getTournamentId()).orElseThrow();
    //        var system = TournamentSystemFactory.create(tournament);
    //        system.updateFixture(bundle);
    //        tournamentRepository.save(tournament);
    //    }

    //    @Override
    //    public Optional<Tournament> findUserById(String tournamentId) {
    //        return tournamentRepository.findUserById(tournamentId);
    //    }
    //
    //    @Override
    //    public Map<TournamentStatus, List<Tournament>> getAllUserTournamentsGroupedByStatus(String userId) {
    //        // TODO(mr): 16.11.2018  to test
    //        return tournamentRepository.findAll().stream()
    //                // TODO(mr): 16.11.2018 or contributor
    //                .filter(tournament -> tournament.getOwner().getId().equals(userId))
    //                .collect(Collectors.groupingBy(Tournament::getStatus, Collectors.toList()));
    //
    //    }

    //    // TODO(mr): 21.11.2018 test
    //    @Override
    //    public Fixture getFixtureById(String tournamentId, String fixtureId) {
    //        return getFixtures(tournamentId).stream()
    //                .filter(fixture -> fixture.getId().equals(fixtureId))
    //                .findFirst()
    //                .orElse(null);
    //    }

    //    @Override
    //    public List<Fixture> getFixtures(String tournamentId) {
    //        return tournamentRepository.findUserById(tournamentId)
    //                .map(Tournament::getSystemState)
    //                .map(SystemState::getFixtures)
    //                .orElse(Collections.emptyList());
    //    }

    //    @Override
    //    public GenericTournamentTable getTournamentTable(String tournamentId) {
    //        return tournamentRepository.findUserById(tournamentId)
    //                .map(Tournament::getSystemState)
    //                .map(SystemState::getTournamentTable)
    //                .orElse(null);
    //    }

    //    @Override
    //    public List<Fixture> getCurrentFixtures(String tournamentId) {
    //        return getCurrentRound(tournamentId)
    //                .map(round -> getFixtures(tournamentId).stream()
    //                        .filter(fixture -> fixture.getRound() == round)
    //                        .collect(Collectors.toList()))
    //                .orElse(Collections.emptyList());
    //    }

    //    @Override
    //    public List<Fixture> prepareNextRound(String tournamentId) {
    //        return tournamentRepository.findUserById(tournamentId)
    //                .map(TournamentSystemFactory::create)
    //                .map(TournamentSystem::prepareNextRound)
    //                .orElse(Collections.emptyList());
    //    }

    //    @Override
    //    public void addNextRoundFixtures(String tournamentId, List<Fixture> fixtures) {
    //        var tournament = tournamentRepository.findUserById(tournamentId);
    //        tournament.ifPresent(t -> {
    //            var system = TournamentSystemFactory.create(t);
    //            system.createNextRoundFixtures(fixtures);
    //            tournamentRepository.save(t);
    //        });
    //    }

    //    @Override
    //    public Optional<Integer> getCurrentRound(String tournamentId) {
    //        return tournamentRepository.findUserById(tournamentId)
    //                .map(Tournament::getSystemState)
    //                .map(SystemState::getCurrentRound);
    //    }

    //    @Override
    //    public List<Tournament> getUserTournaments(String userId) {
    //        return tournamentRepository.findAll().stream()
    //                .filter(t -> t.getParticipants().stream()
    //                        .map(Participant::getId)
    //                        .anyMatch(pId -> pId.equals(userId))
    //                        ||
    //                        t.getOwner().getId().equals(userId)
    //                )
    //                .collect(Collectors.toList());
    //    }

    //    @Override
    //    public Optional<Participant> findParticipantByUserId(String tournamentId, String userId) {
    //        return findUserById(tournamentId).flatMap(tournament -> tournament.getParticipants().stream()
    //                .filter(p -> p.getId().equals(userId))
    //                .findFirst());
    //    }
}
