package pl.wturnieju.tournament.system;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pl.wturnieju.service.impl.SwissSystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SwissSystemState;
import pl.wturnieju.tournament.system.table.SwissTournamentTableRow;
import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableRow;

public class SwissTournamentSystem extends TournamentSystem<SwissSystemState> {

    public SwissTournamentSystem(SwissSystemStateService stateService, Tournament tournament) {
        super(stateService, tournament);
    }

    @Override
    public void finishTournament() {
        // TODO(mr): 13.01.2019 impl

    }

    @Override
    protected void createSystemState() {
        var state = new SwissSystemState();

        state.setTeamsWithBye(Collections.emptyList());
        state.setGameFixtures(Collections.emptyList());
        state.setTeamsPlayedEachOther(Collections.emptyMap());

        stateService.insertSystemState(state);
    }

    @Override
    public TournamentTable<TournamentTableRow>
    buildTournamentTable() {
        // TODO(mr): 14.01.2019 fix impl
        Map<String, SwissTournamentTableRow> teamIdToRow = new HashMap<>();

        var games = getSystemState().getGameFixtures();

        games.forEach(game -> {
            createRowsIfNotExists(teamIdToRow, game);
            var homeRow = teamIdToRow.get(game.getHomeTeam().getId());
            var awayRow = (SwissTournamentTableRow) null;
            if (game.getAwayTeam() != null) {
                awayRow = teamIdToRow.get(game.getAwayTeam().getId());
            }

            if (awayRow == null) {
                homeRow.incWins();
                homeRow.addPoints(1.0);
            } else {
                if (game.getWinner() == 0) {
                    homeRow.incDraws();
                    awayRow.incDraws();
                    homeRow.addPoints(0.5);
                    awayRow.addPoints(0.5);
                } else if (game.getWinner() == 1) {
                    homeRow.incWins();
                    awayRow.incLoses();
                    homeRow.addPoints(1.0);
                } else {
                    homeRow.incLoses();
                    awayRow.incWins();
                    awayRow.addPoints(1.0);
                }
                awayRow.incTotalGames();
            }
            homeRow.incTotalGames();
        });
        return createTournamentTable(teamIdToRow.values());
    }
}
