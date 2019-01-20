package pl.wturnieju.controller.dto.game.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.controller.dto.tournament.gamefixture.ScoreDto;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
@JsonTypeInfo(
        use = Id.NAME,
        property = "competitionType",
        visible = true
)
@JsonSubTypes({
        //        @Type(value = StartTennisGameEventDto.class, name = "COMPETITION_TYPE.TENNIS"),
        @Type(value = FinishChessGameEventDto.class, name = "COMPETITION_TYPE.CHESS"),
})
public abstract class FinishGameEventDto extends GameEventDto {

    private Timestamp finishedDate;

    private ScoreDto homeScore;

    private ScoreDto awayScore;

    private Integer winner;
}
