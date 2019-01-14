package pl.wturnieju.controller.dto.tournament;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.TournamentStatus;

@Mapper(componentModel = "spring", uses = {TournamentDtoMapper.class})
public abstract class UserTournamentsDtoMapper {

    public UserTournamentsDto tournamentsToUserTournamentDto(String userId, List<Tournament> tournaments) {
        Map<TournamentStatus, List<Tournament>> tournamentsMap = tournaments.stream()
                .collect(Collectors.groupingBy(Tournament::getStatus, Collectors.toList()));
        return tournamentsToUserTournamentDto(userId, tournamentsMap);
    }

    protected abstract UserTournamentsDto tournamentsToUserTournamentDto(String userId,
            Map<TournamentStatus, List<Tournament>> tournaments);

}
