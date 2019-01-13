package pl.wturnieju.controller.dto.tournament;

import java.util.List;
import java.util.stream.Collectors;

import pl.wturnieju.dto.TournamentDTO;
import pl.wturnieju.dto.mapping.TournamentDTOMapping;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.tournament.Tournament;

public class UserTournamentsDTOBuilder {

    private UserTournamentsDTO dto;

    public UserTournamentsDTOBuilder() {
        dto = new UserTournamentsDTO();
    }

    public UserTournamentsDTOBuilder userId(String userId) {
        dto.setUserId(userId);
        return this;
    }

    public UserTournamentsDTOBuilder tournaments(
            IUserService userService,
            List<Tournament> tournaments) {
        var tournamentStatusToTournament = tournaments.stream()
                .map(tournament -> TournamentDTOMapping.map(userService, tournament))
                .collect(Collectors.groupingBy(TournamentDTO::getStatus, Collectors.toList()));
        dto.setTournaments(tournamentStatusToTournament);
        return this;
    }

    public UserTournamentsDTO build() {
        return dto;
    }

}
