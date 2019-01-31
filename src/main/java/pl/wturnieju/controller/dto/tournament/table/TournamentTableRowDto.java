package pl.wturnieju.controller.dto.tournament.table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TournamentTableRowDto {

    private Integer baseOrderNum;

    private String teamId;

    private String name;

    private Integer draws;

    private Integer wins;

    private Integer loses;

    private Double points;

    private Double smallPoints;

    private Integer totalGames;
}
