package pl.wturnieju.controller.dto.tournament;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.dto.TournamentDTO;
import pl.wturnieju.tournament.TournamentStatus;

@Getter
@Setter
public class UserTournamentsDTO {

    private String userId;

    private Map<TournamentStatus, List<TournamentDTO>> tournaments;
}
