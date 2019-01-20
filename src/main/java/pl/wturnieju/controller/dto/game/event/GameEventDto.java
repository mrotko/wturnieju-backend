package pl.wturnieju.controller.dto.game.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.CompetitionType;

@Getter
@Setter
@JsonTypeInfo(
        use = Id.NAME,
        visible = true,
        property = "gameEventType"
)
@JsonSubTypes({
        @Type(value = StartGameEventDto.class, name = "GAME_EVENT_TYPE.GAME_START"),
        @Type(value = FinishGameEventDto.class, name = "GAME_EVENT_TYPE.GAME_FINISHED"),
        @Type(value = TeamEventDto.class, name = "GAME_EVENT_TYPE.TEAM_EVENT")
})
public abstract class GameEventDto {

    private String tournamentId;

    private CompetitionType competitionType;

    private String gameId;

    private GameEventType gameEventType;
}
