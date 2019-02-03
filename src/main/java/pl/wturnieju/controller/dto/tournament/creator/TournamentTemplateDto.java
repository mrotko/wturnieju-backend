package pl.wturnieju.controller.dto.tournament.creator;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Data;
import pl.wturnieju.controller.dto.config.TableColumnType;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.ParticipantType;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentSystemType;
import pl.wturnieju.tournament.GameResultType;


@Data
@JsonTypeInfo(use = Id.NAME, property = "competitionType", visible = true)
@JsonSubTypes({
        @Type(value = ChessTournamentTemplateDto.class, name = "COMPETITION_TYPE.CHESS"),
        @Type(value = FootballTournamentTemplateDto.class, name = "COMPETITION_TYPE.FOOTBALL"),
        @Type(value = TennisTournamentTemplateDto.class, name = "COMPETITION_TYPE.TENNIS"),
        @Type(value = CustomTournamentTemplateDto.class, name = "COMPETITION_TYPE.CUSTOM"),
})
public class TournamentTemplateDto {

    private String name;

    private String description;

    private String place;

    private Timestamp startDate;

    private Timestamp endDate;

    private AccessOption accessOption;

    private int maxParticipants;

    private int minParticipants;

    private CompetitionType competitionType;

    private Boolean invitationLink;

    private TournamentSystemType systemType;

    private ParticipantType participantType;

    private List<TableColumnType> tableColumns;

    private Map<GameResultType, Double> scoring;
}
