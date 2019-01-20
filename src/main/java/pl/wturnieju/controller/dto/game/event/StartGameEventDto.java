package pl.wturnieju.controller.dto.game.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
@JsonTypeInfo(
        use = Id.NAME,
        property = "competitionType",
        visible = true
)
@JsonSubTypes({
        @Type(value = StartTennisGameEventDto.class, name = "COMPETITION_TYPE.TENNIS"),
        @Type(value = StartChessGameEventDto.class, name = "COMPETITION_TYPE.CHESS"),
})
public abstract class StartGameEventDto extends GameEventDto {

    private Timestamp startDate;

}
