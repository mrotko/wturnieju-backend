package pl.wturnieju.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.model.ChessTournament;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentSystemType;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChessTournamentTemplateDto extends TournamentTemplateDto<ChessTournament> {

    private TournamentSystemType tournamentSystem;

    private TournamentParticipantType participantType;

    public ChessTournamentTemplateDto() {
        setCompetition(CompetitionType.CHESS);
    }

    @Override
    public void assignFields(ChessTournament entity) {
        super.assignFields(entity);
        entity.setSystemType(tournamentSystem);
        entity.setTournamentParticipantType(participantType);
    }
}
