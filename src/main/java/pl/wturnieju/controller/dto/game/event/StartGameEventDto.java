package pl.wturnieju.controller.dto.game.event;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public class StartGameEventDto extends GameEventDto {

    private Timestamp startDate;

}
