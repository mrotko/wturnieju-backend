package pl.wturnieju.tournament.system.table;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TournamentTableRowManager {

    private final TournamentTableRow row;

    public void increaseTotalGames() {
        row.setTotalGames(row.getTotalGames() + 1);
    }

    public void increaseDraws() {
        row.setDraws(row.getDraws() + 1);
    }

    public void increaseLoses() {
        row.setLoses(row.getLoses() + 1);
    }

    public void increaseWins() {
        row.setWins(row.getWins() + 1);
    }

    public void addPoints(Double points) {
        row.setPoints(row.getPoints() + points);
    }
}
