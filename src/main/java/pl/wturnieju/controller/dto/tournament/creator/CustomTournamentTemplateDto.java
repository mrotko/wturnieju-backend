package pl.wturnieju.controller.dto.tournament.creator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.model.CompetitionType;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomTournamentTemplateDto extends TournamentTemplateDto {
    public CustomTournamentTemplateDto() {
        setCompetitionType(CompetitionType.CUSTOM);
    }
}
