package pl.wturnieju.dto;

import lombok.Data;

@Data
public class TournamentTableRowDTO {

    private Integer position;

    private String profileId;

    private Double points;

    private Integer wins;

    private Integer draws;

    private Integer loses;

    private Double smallPoints;
}
