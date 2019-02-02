package pl.wturnieju.tournament.system.table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class TournamentTableRow {

    private final String teamId;

    private final String name;

    private Integer loses = 0;

    private Integer wins = 0;

    private Integer draws = 0;

    private Double points = 0.;

    private Integer totalGames = 0;

    private Double smallPoints = 0.;
}
