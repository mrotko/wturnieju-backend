package pl.wturnieju.dto;

import lombok.Data;
import pl.wturnieju.model.IProfile;

@Data
public class TournamentTableRowDTO {

    private Integer position;

    private IProfile profile;

    private Double points;

    private Integer wins;

    private Integer draws;

    private Integer loses;

    private Double smallPoints;
}
