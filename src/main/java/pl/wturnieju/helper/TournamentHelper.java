package pl.wturnieju.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.Tournament;

@Component
@RequiredArgsConstructor
public class TournamentHelper implements ITournamentHelper {

    private final ITournamentService tournamentService;

    private final IParticipantService participantService;

    @Override
    public List<Tournament> getUserTournaments(String userId) {
        Map<String, Tournament> tournamentIdToTournamentMapping = new HashMap<>();
        var participants = participantService.getAllByUserId(userId);
        participants.stream()
                .map(Participant::getTournamentId)
                .map(tournamentService::getById)
                .filter(Objects::nonNull)
                .forEach(tournament -> tournamentIdToTournamentMapping.put(tournament.getId(), tournament));

        var tournamentsOwnedByUser = tournamentService.getTournamentsOwnedByUserId(userId);
        tournamentsOwnedByUser.forEach(
                tournament -> tournamentIdToTournamentMapping.put(tournament.getId(), tournament));

        return new ArrayList<>(tournamentIdToTournamentMapping.values());
    }
}
