package pl.wturnieju.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
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
    public Participant invite(String tournamentId, String userId) {
        var tournament = tournamentService.findTournament(tournamentId).orElse(null);

        if (tournament == null) {
            throw new IllegalArgumentException();
        }

        if (getTournamentParticipant(tournament, userId).isPresent()) {
            throw new IllegalArgumentException(String.format("%s participant exists", userId));
        }
        Participant participant = new Participant();
        participant.setId(new ObjectId().toString());
        participant.setLeaderId(userId);
        participant.setName(userService.findUserById(userId).map(User::getFullName).orElse(null));
        participant.setParticipantStatus(ParticipantStatus.INVITED);
        participant.setInvitationStatus(InvitationStatus.INVITED);
        participant.setParticipantType(tournament.getParticipantType());
        participant.setMembers(Collections.emptyList());
        tournament.getParticipants().add(participant);

        tournamentService.updateTournament(tournament);

        return participant;
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
            if (findParticipantByLeaderId(tournament, userId).isPresent()) {
                throw new ResourceExistsException("This user participate in tournament");
            }
            var participant = new Participant();
            participant.setInvitationStatus(InvitationStatus.PARTICIPATION_REQUEST);
            participant.setParticipantStatus(ParticipantStatus.INVITED);
            participant.setParticipantType(tournament.getParticipantType());
            participant.setMembers(Collections.emptyList());
            participant.setName(userService.findUserById(userId).map(User::getFullName).orElse(null));
            participant.setLeaderId(userId);
            participant.setId(new ObjectId().toString());
            tournament.getParticipants().add(participant);
            tournamentService.updateTournament(tournament);
        });
    }

    private Optional<Participant> findParticipantByLeaderId(Tournament tournament, String leaderId) {
        return tournament.getParticipants().stream()
                .filter(participant -> participant.getLeaderId().equals(leaderId))
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
