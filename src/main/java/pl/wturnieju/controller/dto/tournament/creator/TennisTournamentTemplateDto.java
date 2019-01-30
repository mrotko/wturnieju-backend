package pl.wturnieju.controller.dto.tournament.creator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.model.CompetitionType;

@Data
@EqualsAndHashCode(callSuper = true)
public class TennisTournamentTemplateDto extends TournamentTemplateDto {
    public TennisTournamentTemplateDto() {
        setCompetitionType(CompetitionType.TENNIS);
    }
}
