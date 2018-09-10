package pl.wturnieju.dto;

import lombok.Data;
import pl.wturnieju.model.ChessTournament;

@Data
public class ChessTournamentTemplateDto extends TournamentTemplateDto<ChessTournament> {

    private int moveTimeSec;

    private int incTimeSec;

    @Override
    public void assignFields(ChessTournament entity) {
        super.assignFields(entity);
        entity.setMoveTimeSec(moveTimeSec);
        entity.setIncTimeSec(incTimeSec);
    }
}
