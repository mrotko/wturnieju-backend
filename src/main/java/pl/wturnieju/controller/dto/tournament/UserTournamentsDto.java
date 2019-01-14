package pl.wturnieju.controller.dto.tournament;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.tournament.TournamentStatus;

@Getter
@Setter
public class UserTournamentsDto {

    private String userId;

    private Map<TournamentStatus, List<TournamentDto>> tournaments;
}
