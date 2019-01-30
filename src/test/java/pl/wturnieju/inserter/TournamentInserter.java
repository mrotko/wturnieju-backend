package pl.wturnieju.inserter;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.controller.dto.config.TournamentConfigDto;
import pl.wturnieju.controller.dto.tournament.creator.ChessTournamentTemplateDto;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentSystemType;
import pl.wturnieju.service.ITournamentCreatorService;

@RequiredArgsConstructor
public class TournamentInserter {

    private final ITournamentCreatorService tournamentCreatorService;

    private TournamentConfigDto config = new TournamentConfigDto();

    public void insertTournamentToDatabase() {
        //        tournamentCreatorService.create(prepareChessTournamentDto());
    }

    private ChessTournamentTemplateDto prepareChessTournamentDto() {
        var dto = new ChessTournamentTemplateDto();

        dto.setTournamentParticipantType(TournamentParticipantType.SINGLE);
        dto.setCompetitionType(CompetitionType.CHESS);
        dto.setSystemType(TournamentSystemType.SWISS);
        dto.setAccessOption(AccessOption.PRIVATE);
        dto.setDescription("");
        dto.setStartDate(new Timestamp(LocalDateTime.now().minusDays(1)));
        dto.setMaxParticipants(5);
        dto.setMinParticipants(2);
        dto.setName("TURNIEJ SZACHOWY");
        dto.setPlace("");
        dto.setEndDate(new Timestamp(LocalDateTime.now().plusDays(1)));

        return dto;
    }
}
