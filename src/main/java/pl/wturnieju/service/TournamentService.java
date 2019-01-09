package pl.wturnieju.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.handler.TournamentSystem;
import pl.wturnieju.handler.TournamentSystemFactory;
import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.model.ParticipantStatus;
import pl.wturnieju.model.TournamentParticipant;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.model.generic.GenericTournamentTable;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentSystemState;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.validator.Validators;

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
        getById(bundle.getTournamentId()).ifPresent(tournament -> {
            var system = TournamentSystemFactory.getInstance(tournament);
            validateTournamentParticipants(tournament);
            prepareParticipantsBeforeStart(tournament);
            system.updateTournament(bundle);
            tournamentRepository.save(tournament);
        });
    }

    
    private void validateTournamentParticipants(Tournament tournament) {
        var validator = Validators.getTournamentParticipantsValidator();
        try {
            validator.validateAndThrowInvalid(tournament);
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void prepareParticipantsBeforeStart(Tournament tournament) {
        var participants = tournament.getParticipants();
        participants.removeIf(p -> p.getInvitationStatus() != InvitationStatus.ACCEPTED);
        participants.forEach(p -> p.setParticipantStatus(ParticipantStatus.ACTIVE));
    }

    // TODO(mr): 21.11.2018 test
    @Override
    public void updateFixture(GenericFixtureUpdateBundle bundle) {
        var tournament = tournamentRepository.findById(bundle.getTournamentId()).orElseThrow();
        var system = TournamentSystemFactory.getInstance(tournament);
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
        return getFixtures(tournamentId).stream()
                .filter(fixture -> fixture.getId().equals(fixtureId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Fixture> getFixtures(String tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(Tournament::getTournamentSystemState)
                .map(TournamentSystemState::getFixtures)
                .orElse(Collections.emptyList());
    }

    @Override
    public GenericTournamentTable getTournamentTable(String tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(Tournament::getTournamentSystemState)
                .map(TournamentSystemState::getTournamentTable)
                .orElse(null);
    }

    @Override
    public List<Fixture> getCurrentFixtures(String tournamentId) {
        return getCurrentRound(tournamentId)
                .map(round -> getFixtures(tournamentId).stream()
                        .filter(fixture -> fixture.getRound() == round)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<Fixture> prepareNextRound(String tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(TournamentSystemFactory::getInstance)
                .map(TournamentSystem::prepareNextRound)
                .orElse(Collections.emptyList());
    }

    @Override
    public void addNextRoundFixtures(String tournamentId, List<Fixture> fixtures) {
        var tournament = tournamentRepository.findById(tournamentId);
        tournament.ifPresent(t -> {
            var system = TournamentSystemFactory.getInstance(t);
            system.createNextRoundFixtures(fixtures);
            tournamentRepository.save(t);
        });
    }

    @Override
    public Optional<Integer> getCurrentRound(String tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(Tournament::getTournamentSystemState)
                .map(TournamentSystemState::getCurrentRound);
    }

    @Override
    public List<Tournament> getUserTournaments(String userId) {
        return tournamentRepository.findAll().stream()
                .filter(t -> t.getParticipants().stream()
                        .map(TournamentParticipant::getId)
                        .anyMatch(pId -> pId.equals(userId))
                        ||
                        t.getOwner().getId().equals(userId)
                )
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TournamentParticipant> findParticipantByUserId(String tournamentId, String userId) {
        return getById(tournamentId).flatMap(tournament -> tournament.getParticipants().stream()
                .filter(p -> p.getId().equals(userId))
                .findFirst());
    }
}
