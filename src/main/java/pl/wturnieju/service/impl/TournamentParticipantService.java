package pl.wturnieju.service.impl;

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
import pl.wturnieju.model.User;
import pl.wturnieju.service.ITournamentParticipantService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.Tournament;

@RequiredArgsConstructor
@Service
public class TournamentParticipantService implements ITournamentParticipantService {

    private final ITournamentService tournamentService;

    private final IUserService userService;

    @Override
    public List<Participant> getAll(String tournamentId) {
        return tournamentService.findTournament(tournamentId)
                .map(Tournament::getParticipants).orElse(Collections.emptyList());
    }

    @Override
    public void doResign(String tournamentId, String participantId) {
        tournamentService.findTournament(tournamentId).ifPresent(tournament -> {
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
        tournamentService.findTournament(tournamentId).ifPresent(tournament -> {
            getTournamentParticipant(tournament, participantId).ifPresent(tournamentParticipant -> {
                if (tournamentParticipant.getParticipantStatus() == ParticipantStatus.ACTIVE) {
                    tournamentParticipant.setParticipantStatus(ParticipantStatus.DISQUALIFIED);
                }
            });
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public Optional<Participant> getById(String tournamentId, String participantId) {
        return tournamentService.findTournament(tournamentId)
                .flatMap(tournament -> getTournamentParticipant(tournament, participantId));
    }

    @Override
    public void invite(String tournamentId, String participantId) {
        tournamentService.findTournament(tournamentId).ifPresent(tournament -> {
            if (getTournamentParticipant(tournament, participantId).isPresent()) {
                throw new IllegalArgumentException(String.format("%s participant exists", participantId));
            }
            Participant tournamentParticipant = new Participant();
            tournamentParticipant.setId(participantId);
            tournamentParticipant.setParticipantStatus(ParticipantStatus.INVITED);
            tournamentParticipant.setInvitationStatus(InvitationStatus.INVITED);
            tournament.getParticipants().add(tournamentParticipant);
            tournament.setName(userService.findUserById(participantId).map(User::getFullName).orElse(null));
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public void deleteParticipant(String tournamentId, String participantId) {
        tournamentService.findTournament(tournamentId).ifPresent(tournament -> {
            tournament.getParticipants().removeIf(p -> Objects.equals(p.getId(), participantId));
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public void acceptInvitation(String tournamentId, String participantId) {
        tournamentService.findTournament(tournamentId).ifPresent(tournament -> {
            var participant = getTournamentParticipant(tournament, participantId).orElseThrow(
                    () -> new ResourceNotFoundException("Tournament participant not found"));
            participant.setInvitationStatus(InvitationStatus.ACCEPTED);
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public void rejectInvitation(String tournamentId, String participantId) {
        tournamentService.findTournament(tournamentId).ifPresent(tournament -> {
            var participant = getTournamentParticipant(tournament, participantId).orElseThrow(
                    () -> new ResourceNotFoundException("Participant not found"));
            participant.setInvitationStatus(InvitationStatus.REJECTED);
            tournamentService.updateTournament(tournament);
        });
    }

    @Override
    public void requestParticipation(String tournamentId, String userId) {
        tournamentService.findTournament(tournamentId).ifPresent(tournament -> {
            if (findParticipantByUserId(tournament, userId).isPresent()) {
                throw new ResourceExistsException("This user participate in tournament");
            }
            var participant = new Participant();
            participant.setInvitationStatus(InvitationStatus.PARTICIPATION_REQUEST);
            participant.setParticipantStatus(ParticipantStatus.INVITED);
            participant.setId(userId);
            tournament.getParticipants().add(participant);
            tournamentService.updateTournament(tournament);
        });
    }

    private Optional<Participant> findParticipantByUserId(Tournament tournament, String userId) {
        return tournament.getParticipants().stream()
                .filter(participant -> participant.getId().equals(userId))
                .findFirst();
    }

    private Optional<Participant> getTournamentParticipant(Tournament tournament, String participantId) {
        return Optional.ofNullable(tournament).map(Tournament::getParticipants)
                .map(Collection::stream)
                .map(stream -> stream.filter(tournamentParticipant ->
                        tournamentParticipant.getId().equals(participantId)))
                .flatMap(Stream::findFirst);
    }
}
