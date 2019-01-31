package pl.wturnieju.tournament.system.table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class TournamentTableRow {

    protected String teamId = null;

    protected String name = null;

    protected Integer loses = 0;

    protected Integer wins = 0;

    protected Integer draws = 0;

    protected Double points = 0.;

    protected Integer totalGames = 0;

    private Double smallPoints = 0.;

    public void incTotalGames() {
        totalGames++;
    }

    public void incDraws() {
        draws++;
    }

    public void incWins() {
        wins++;
    }

    public void incLoses() {
        loses++;
    }

    public void addPoints(Double points) {
        this.points += points;
    }

    public TournamentTableRow(String teamId, String name) {
        this.teamId = teamId;
        this.name = name;
    }
}
