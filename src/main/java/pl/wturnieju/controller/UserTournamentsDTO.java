package pl.wturnieju.controller;

import java.util.List;
import java.util.Map;

import lombok.Data;
import pl.wturnieju.dto.TournamentDTO;
import pl.wturnieju.model.TournamentStatus;

@Data
public class UserTournamentsDTO {

    private Map<TournamentStatus, List<TournamentDTO>> tournaments;

}
