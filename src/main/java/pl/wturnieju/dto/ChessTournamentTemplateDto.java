package pl.wturnieju.dto;

import lombok.Data;
import pl.wturnieju.model.ChessTournament;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentSystemType;

@Data
public class ChessTournamentTemplateDto extends TournamentTemplateDto<ChessTournament> {

    private TournamentSystemType tournamentSystemType;

    private TournamentParticipantType tournamentParticipantType;

    @Override
    public void assignFields(ChessTournament entity) {
        super.assignFields(entity);
        entity.setSystemType(tournamentSystemType);
        entity.setTournamentParticipantType(tournamentParticipantType);
    }
}
