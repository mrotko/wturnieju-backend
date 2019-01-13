package pl.wturnieju.service;

import java.util.List;
import java.util.Optional;

import pl.wturnieju.tournament.Participant;

public interface ITournamentParticipantService {

    List<Participant> getAll(String tournamentId);

    void doResign(String tournamentId, String participantId);

    void doDisqualify(String tournamentId, String participantId, String reason);

    Optional<Participant> getById(String tournamentId, String participantId);

    void invite(String tournamentId, String participantId);

    void deleteParticipant(String tournamentId, String participantId);

    void acceptInvitation(String tournamentId, String participantId);

    void rejectInvitation(String tournamentId, String participantId);

    void requestParticipation(String tournamentId, String userId);
}