package pl.wturnieju.controller;

import java.util.List;
import java.util.Map;

import lombok.Data;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.generic.Tournament;

@Data
public class UserTournamentsDTO {

    private Map<TournamentStatus, List<Tournament>> tournaments;

}
