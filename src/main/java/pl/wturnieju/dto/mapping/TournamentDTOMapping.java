package pl.wturnieju.dto.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import pl.wturnieju.dto.TournamentDTO;
import pl.wturnieju.model.ChessTournament;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.service.IUserService;

public class TournamentDTOMapping {

    private static Map<String, Function<Tournament, TournamentDTO>> mappingStrategy = new HashMap<>();

    public static TournamentDTO map(
            IUserService userService,
            Tournament tournament) {
        if (tournament == null) {
            return null;
        }
        return mappingStrategy.computeIfAbsent(tournament.getClass().getSimpleName(),
                simpleClassName -> getMappingStrategy(userService, simpleClassName)).apply(tournament);
    }

    static Function<Tournament, TournamentDTO> getMappingStrategy(
            IUserService userService,
            String simpleClassName) {

        if (simpleClassName.equals(ChessTournament.class.getSimpleName())) {
            return (tournament -> {
                var dto = new TournamentDTO();

                dto.setId(tournament.getId());
                dto.setName(tournament.getName());
                dto.setAccessOption(tournament.getAccessOption());
                dto.setCompetitionType(tournament.getCompetitionType());
                dto.setDescription(tournament.getDescription());
                dto.setStartDate(tournament.getStartDate());
                dto.setEndDate(tournament.getEndDate());
                dto.setImg(tournament.getImg());
                dto.setMaxParticipants(tournament.getMaxParticipants());
                dto.setMinParticipants(tournament.getMinParticipants());
                dto.setOwner(tournament.getOwner());
                dto.setParticipants(tournament.getParticipants().stream()
                        .map(participant -> TournamentParticipantMapping
                                .map(userService.getById(participant.getId()).orElse(null), participant))
                        .collect(Collectors.toList())
                );
                dto.setParticipantType(tournament.getTournamentParticipantType());
                dto.setPlace(tournament.getPlace());
                dto.setStatus(tournament.getStatus());
                dto.setSystemType(tournament.getSystemType());
                dto.setPlannedRounds(tournament.getPlannedRounds());
                dto.setInvitationToken(tournament.getInvitationToken());

                return dto;
            });
        }

        throw new IllegalArgumentException("Unknown tournament table [" + simpleClassName + "]");
    }
}
