package pl.wturnieju.controller.dto.table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TournamentTableRowDTO {

    private String teamId;

    private Integer lp;

    private String name;

    private Integer draws;

    private Integer wins;

    private Integer loses;

    private Double points;
}
