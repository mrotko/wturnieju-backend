package pl.wturnieju.controller.dto.tournament;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTournamentStatusDTO {

    private String tournamentId;

    private String status;
}
