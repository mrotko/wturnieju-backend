package pl.wturnieju.dto;

import lombok.Data;
import pl.wturnieju.model.ChessTournament;
import pl.wturnieju.model.CompetitionType;

@Data
public class ChessTournamentTemplateDto extends TournamentTemplateDto<ChessTournament> {

    private int moveTimeSec;

    private int incTimeSec;

    public ChessTournamentTemplateDto() {
        setCompetitionType(CompetitionType.CHESS);
    }

    @Override
    public void assignFields(ChessTournament entity) {
        super.assignFields(entity);
        entity.setMoveTimeSec(moveTimeSec);
        entity.setIncTimeSec(incTimeSec);
    }
}
