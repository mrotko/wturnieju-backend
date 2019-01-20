package pl.wturnieju.controller.dto.game.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class TeamEventDto extends GameEventDto {

    private String teamId;
}
