package pl.wturnieju.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.exception.ResourceExistsException;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.model.ParticipantStatus;
import pl.wturnieju.model.TournamentParticipant;
import pl.wturnieju.model.generic.Tournament;

@RequiredArgsConstructor
@Service
public class TournamentParticipantService implements ITournamentParticipantService {

    private final ITournamentService tournamentService;

    @Override
    public List<TournamentParticipant> getAll(String tournamentId) {
        return tournamentService.getById(tournamentId)
                .map(Tournament::getParticipants).orElse(Collections.emptyList());
    }

    @Override
    public void doResign(String tournamentId, String participantId) {
        tournamentService.getById(tournamentId).ifPresent(tournament -> {
            getTournamentParticipant(tournament, participantId).ifPresent(tournamentParticipant -> {
                switch (tournamentParticipant.getParticipantStatus()) {
                case INVITED:
                    tournament.getParticipants().remove(tournamentParticipant);
                    break;
                case ACTIVE:
                    tournamentParticipant.setParticipantStatus(ParticipantStatus.RESIGNED);
                    break;
                default:
                    break;
                }
            });
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public void doDisqualify(String tournamentId, String participantId, String reason) {
        tournamentService.getById(tournamentId).ifPresent(tournament -> {
            getTournamentParticipant(tournament, participantId).ifPresent(tournamentParticipant -> {
                if (tournamentParticipant.getParticipantStatus() == ParticipantStatus.ACTIVE) {
                    tournamentParticipant.setParticipantStatus(ParticipantStatus.DISQUALIFIED);
                }
            });
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public Optional<TournamentParticipant> getById(String tournamentId, String participantId) {
        return tournamentService.getById(tournamentId)
                .flatMap(tournament -> getTournamentParticipant(tournament, participantId));
    }

    @Override
    public void invite(String tournamentId, String participantId) {
        tournamentService.getById(tournamentId).ifPresent(tournament -> {
            if (getTournamentParticipant(tournament, participantId).isPresent()) {
                throw new IllegalArgumentException(String.format("%s participant exists", participantId));
            }
            TournamentParticipant tournamentParticipant = new TournamentParticipant();
            tournamentParticipant.setId(participantId);
            tournamentParticipant.setParticipantStatus(ParticipantStatus.INVITED);
            tournamentParticipant.setInvitationStatus(InvitationStatus.INVITED);
            tournament.getParticipants().add(tournamentParticipant);
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public void deleteParticipant(String tournamentId, String participantId) {
        tournamentService.getById(tournamentId).ifPresent(tournament -> {
            tournament.getParticipants().removeIf(p -> Objects.equals(p.getId(), participantId));
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public void acceptInvitation(String tournamentId, String participantId) {
        tournamentService.getById(tournamentId).ifPresent(tournament -> {
            var participant = getTournamentParticipant(tournament, participantId).orElseThrow(
                    () -> new ResourceNotFoundException("Tournament participant not found"));
            participant.setInvitationStatus(InvitationStatus.ACCEPTED);
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public void rejectInvitation(String tournamentId, String participantId) {
        tournamentService.getById(tournamentId).ifPresent(tournament -> {
            var participant = getTournamentParticipant(tournament, participantId).orElseThrow(
                    () -> new ResourceNotFoundException("TournamentParticipant not found"));
            participant.setInvitationStatus(InvitationStatus.REJECTED);
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public void requestParticipation(String tournamentId, String userId) {
        tournamentService.getById(tournamentId).ifPresent(tournament -> {
            if (findParticipantByUserId(tournament, userId).isPresent()) {
                throw new ResourceExistsException("This user participate in tournament");
            }
            var participant = new TournamentParticipant();
            participant.setInvitationStatus(InvitationStatus.PARTICIPATION_REQUEST);
            participant.setParticipantStatus(ParticipantStatus.INVITED);
            participant.setId(userId);
            tournament.getParticipants().add(participant);
            tournamentService.updateTournament(tournament);
        });
    }

    private Optional<TournamentParticipant> findParticipantByUserId(Tournament tournament, String userId) {
        return tournament.getParticipants().stream()
                .filter(participant -> participant.getId().equals(userId))
                .findFirst();
    }

    private Optional<TournamentParticipant> getTournamentParticipant(Tournament tournament, String participantId) {
        return Optional.ofNullable(tournament).map(Tournament::getParticipants)
                .map(Collection::stream)
                .map(stream -> stream.filter(tournamentParticipant ->
                        tournamentParticipant.getId().equals(participantId)))
                .flatMap(Stream::findFirst);
    }


}
