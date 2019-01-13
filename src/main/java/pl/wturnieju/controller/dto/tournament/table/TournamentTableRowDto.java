package pl.wturnieju.controller.dto.tournament.table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TournamentTableRowDto {

    private String teamId;

    private String name;

    private Integer draws;

    private Integer wins;

    private Integer loses;

    private Double points;

    private Integer totalGames;
}
