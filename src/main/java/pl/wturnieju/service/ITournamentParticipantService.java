package pl.wturnieju.service;

import java.util.List;
import java.util.Optional;

import pl.wturnieju.model.TournamentParticipant;

public interface ITournamentParticipantService {

    List<TournamentParticipant> getAll(String tournamentId);

    void doResign(String tournamentId, String participantId);

    void doDisqualify(String tournamentId, String participantId, String reason);

    Optional<TournamentParticipant> getById(String tournamentId, String participantId);

    void invite(String tournamentId, String participantId);

    void confirm(String tournamentId, String participantId);

    void delete(String tournamentId, String participantId);

    void accept(String tournamentId, String participantId);
}