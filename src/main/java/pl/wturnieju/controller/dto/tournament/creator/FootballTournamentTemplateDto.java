package pl.wturnieju.controller.dto.tournament.creator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.model.CompetitionType;

@Data
@EqualsAndHashCode(callSuper = true)
public class FootballTournamentTemplateDto extends TournamentTemplateDto {

    public FootballTournamentTemplateDto() {
        setCompetitionType(CompetitionType.FOOTBALL);
    }
}
