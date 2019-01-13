package pl.wturnieju.tournament.system.table;

import org.springframework.lang.NonNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class TournamentTableRow implements Comparable<TournamentTableRow> {

    private Integer lp = 0;

    private String teamId = null;

    private String name = null;

    private Integer loses = 0;

    private Integer wins = 0;

    private Integer draws = 0;

    private Double points = 0.;

    private Integer totalGames = 0;

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

    @Override
    public int compareTo(@NonNull TournamentTableRow o) {
        return o.getPoints().compareTo(points);
    }
}
