package pl.wturnieju.generator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import pl.wturnieju.controller.dto.tournament.creator.ChessTournamentTemplateDto;
import pl.wturnieju.controller.dto.tournament.creator.TournamentTemplateDto;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.ParticipantType;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentSystemType;

public class TournamentCreatorDtoGenerator {


    public static List<TournamentTemplateDto> generateAll() {
        List<TournamentTemplateDto> dtos = new ArrayList<>();

        dtos.addAll(generateChessTournaments());

        return dtos;
    }


    public static List<ChessTournamentTemplateDto> generateChessTournaments() {
        List<ChessTournamentTemplateDto> dtos = new ArrayList<>();

        var dto = new ChessTournamentTemplateDto();

        dto.setCompetitionType(CompetitionType.CHESS);
        dto.setDescription("chess competition description");
        dto.setStartDate(new Timestamp(LocalDateTime.now().plusDays(10)));
        dto.setEndDate(new Timestamp(LocalDateTime.now()));
        dto.setMaxParticipants(5);
        dto.setMinParticipants(2);
        dto.setName("chess tournament");
        dto.setPlace("some place");
        dto.setSystemType(TournamentSystemType.SWISS);
        dto.setParticipantType(ParticipantType.SINGLE);

        dtos.add(dto);
        return dtos;
    }
}
