package pl.wturnieju.inserter;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.ChessTournamentTemplateDto;
import pl.wturnieju.dto.TournamentConfigDTO;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentSystemType;
import pl.wturnieju.service.ITournamentCreatorService;

@RequiredArgsConstructor
public class TournamentInserter {

    private final ITournamentCreatorService tournamentCreatorService;

    private TournamentConfigDTO config = new TournamentConfigDTO();

    public void insertTournamentToDatabase() {
        tournamentCreatorService.create(prepareChessTournamentDto());
    }

    private ChessTournamentTemplateDto prepareChessTournamentDto() {
        var dto = new ChessTournamentTemplateDto();

        dto.setParticipantType(TournamentParticipantType.SINGLE);
        dto.setCompetition(CompetitionType.CHESS);
        dto.setTournamentSystem(TournamentSystemType.SWISS);
        dto.setAccessOption(AccessOption.PRIVATE);
        dto.setDescription("");
        dto.setFromDate(new Timestamp(LocalDateTime.now().minusDays(1)));
        dto.setMaxParticipants(5);
        dto.setMinParticipants(2);
        dto.setName("TURNIEJ SZACHOWY");
        dto.setPlace("");
        dto.setToDate(new Timestamp(LocalDateTime.now().plusDays(1)));

        return dto;
    }
}
