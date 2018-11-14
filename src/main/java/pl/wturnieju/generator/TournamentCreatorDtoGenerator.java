package pl.wturnieju.generator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import pl.wturnieju.dto.ChessTournamentTemplateDto;
import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentParticipantType;
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

        dto.setCompetition(CompetitionType.CHESS);
        dto.setDescription("chess competition description");
        dto.setFromDate(new Timestamp(LocalDateTime.now().plusDays(10)));
        dto.setToDate(new Timestamp(LocalDateTime.now()));
        dto.setMaxParticipants(5);
        dto.setMinParticipants(2);
        dto.setName("chess tournament");
        dto.setPlace("some place");
        dto.setTournamentSystem(TournamentSystemType.SWISS);
        dto.setParticipantType(TournamentParticipantType.SINGLE);

        dtos.add(dto);
        return dtos;
    }
}
