package pl.wturnieju.service;

import java.util.List;

import pl.wturnieju.tournament.Participant;

public interface IParticipantService {

    List<Participant> getAllByTournamentId(String tournamentId);

    void doResign(String participantId);

    Participant inviteUserId(String tournamentId, String participantId);

    void deleteParticipant(String participantId);

    void acceptInvitation(String participantId);

    void rejectInvitation(String participantId);

    void requestParticipation(String tournamentId, String userId);

    Participant getById(String participantId);

    Participant getTournamentParticipantByUserId(String tournamentId, String userId);

    void deleteAllById(List<String> toRemove);

    List<Participant> getAllById(List<String> participantIds);

    List<Participant> getAllByGroupId(String groupId);
}