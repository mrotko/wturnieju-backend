package pl.wturnieju.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.TournamentStatus;
import pl.wturnieju.tournament.system.TournamentSystemFactory;
import pl.wturnieju.validator.Validators;

@Service
@RequiredArgsConstructor
public class TournamentService implements ITournamentService {

    private final TournamentRepository tournamentRepository;

    private final ApplicationContext context;

    @Override
    public List<Tournament> getUserTournaments(String userId) {
        return tournamentRepository.findAll().stream()
                .filter(t -> isUserInParticipants(t.getParticipants(), userId) || t.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void startTournament(String tournamentId) {
        var tournament = findTournament(tournamentId).orElseThrow(ResourceNotFoundException::new);
        validateTournamentParticipants(tournament);
        var system = TournamentSystemFactory.create(context, tournament);
        system.startTournament();
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournamentRepository.save(tournament);
    }

    @Override
    public void finishTournament(String tournamentId) {
        var tournament = findTournament(tournamentId).orElseThrow(ResourceNotFoundException::new);
        var system = TournamentSystemFactory.create(context, tournament);
        system.finishTournament();
        tournament.setStatus(TournamentStatus.ENDED);
        tournamentRepository.save(tournament);
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

    @Override
    public List<Tournament> getTournamentsByAccess(AccessOption accessOption) {
        return tournamentRepository.getAllByAccessOption(accessOption);
    }

    @Override
    public String getTournamentName(String tournamentId) {
        var tournament = tournamentRepository.getById(tournamentId);
        if (tournament == null) {
            return null;
        }
        return tournament.getName();
    }

    private boolean isUserInParticipants(List<Participant> participants, String userId) {
        return participants.stream().anyMatch(p -> p.getId().equals(userId));
    }

    private void validateTournamentParticipants(Tournament tournament) {
        var validator = Validators.getTournamentParticipantsValidator();
        try {
            validator.validateAndThrowInvalid(tournament);
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
