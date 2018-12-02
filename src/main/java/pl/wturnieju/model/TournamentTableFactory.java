package pl.wturnieju.model;

import java.util.Collection;

import pl.wturnieju.model.generic.GenericTournamentTable;
import pl.wturnieju.model.generic.SwissTournamentTable;
import pl.wturnieju.model.generic.SwissTournamentTableRow;
import pl.wturnieju.model.generic.Tournament;

public class TournamentTableFactory {

    public static GenericTournamentTable getTable(Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return createSwissTournamentTable(tournament.getParticipants());
        default:
            throw new IllegalArgumentException("unknown tournament system type: + " + tournament.getSystemType());
        }
    }

    private static SwissTournamentTable createSwissTournamentTable(Collection<TournamentParticipant> participants) {
        var table = new SwissTournamentTable();

        participants.forEach(participant -> {
            var row = new SwissTournamentTableRow(participant.getId());

            row.setGames(0);
            row.setPoints(0.);
            row.setWins(0);
            row.setDraws(0);
            row.setLoses(0);
            row.setSmallPoints(0.);

            table.getRows().add(row);
        });

        return table;
    }
}
