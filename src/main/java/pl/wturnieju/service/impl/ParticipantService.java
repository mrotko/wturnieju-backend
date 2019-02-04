package pl.wturnieju.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.ParticipantRepository;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.ParticipantStatus;

@RequiredArgsConstructor
@Service
public class ParticipantService implements IParticipantService {

    private final ITournamentService tournamentService;

    private final ParticipantRepository repository;

    private final IUserService userService;

    @Override
    public List<Participant> getAllByTournamentId(String tournamentId) {
        return repository.getAllByTournamentId(tournamentId);
    }

    @Override
    public void doResign(String participantId) {
        var participant = repository.findById(participantId).orElse(null);

        if (participant != null) {
            switch (participant.getParticipantStatus()) {
            case ACTIVE:
                participant.setParticipantStatus(ParticipantStatus.RESIGNED);
                repository.save(participant);
                break;
            case INVITED:
                deleteParticipant(participantId);
                break;
            default:
                break;
            }
        }
    }

    @Override
    public Participant getById(String participantId) {
        return repository.findById(participantId).orElse(null);
    }

    @Override
    public Participant inviteUserId(String tournamentId, String userId) {
        var tournament = tournamentService.findTournament(tournamentId).orElse(null);

        if (tournament == null) {
            throw new IllegalArgumentException();
        }

        if (getTournamentParticipantByUserId(tournamentId, userId) != null) {
            throw new IllegalArgumentException(String.format("%s participant exists", userId));
        }
        Participant participant = new Participant();
        participant.setId(new ObjectId().toString());
        participant.setLeaderId(userId);
        participant.setName(userService.findUserById(userId).map(User::getFullName).orElse(null));
        participant.setParticipantStatus(ParticipantStatus.INVITED);
        participant.setInvitationStatus(InvitationStatus.INVITED);
        participant.setParticipantType(tournament.getParticipantType());
        participant.setMemberIds(Collections.singletonList(userId));
        participant.setTournamentId(tournamentId);

        return repository.save(participant);
    }

    @Override
    public void deleteParticipant(String participantId) {
        repository.deleteById(participantId);
    }

    @Override
    public void acceptInvitation(String participantId) {
        var participant = getById(participantId);
        if (participant != null) {
            participant.setInvitationStatus(InvitationStatus.ACCEPTED);
            repository.save(participant);
        }
    }

    @Override
    public void rejectInvitation(String participantId) {
        var participant = getById(participantId);
        if (participant != null) {
            participant.setInvitationStatus(InvitationStatus.REJECTED);
            repository.save(participant);
        }
    }

    @Override
    public void requestParticipation(String tournamentId, String userId) {
        var tournament = tournamentService.getByIdOrThrow(tournamentId);

        var participant = new Participant();
        participant.setInvitationStatus(InvitationStatus.PARTICIPATION_REQUEST);
        participant.setParticipantStatus(ParticipantStatus.INVITED);
        participant.setParticipantType(tournament.getParticipantType());
        participant.setName(userService.findUserById(userId).map(User::getFullName).orElse(null));
        participant.setLeaderId(userId);
        participant.setMemberIds(Collections.singletonList(userId));
        participant.setId(new ObjectId().toString());
        participant.setTournamentId(tournamentId);

        repository.save(participant);
    }

    @Override
    public Participant getTournamentParticipantByUserId(String tournamentId, String userId) {
        return getAllByTournamentId(tournamentId).stream()
                .filter(p -> p.getLeaderId().equals(userId))
                .filter(p -> p.getMemberIds().contains(userId))
                .findFirst().orElse(null);
    }

    @Override
    public void deleteAllById(List<String> participantIds) {
        participantIds.forEach(repository::deleteById);
    }

    @Override
    public List<Participant> getAllById(List<String> participantIds) {
        List<Participant> participants = new ArrayList<>();
        repository.findAllById(participantIds).forEach(participants::add);
        return participants;
    }

    @Override
    public List<Participant> getAllByGroupId(String groupId) {
        return repository.getAllByGroupId(groupId);
    }
}
