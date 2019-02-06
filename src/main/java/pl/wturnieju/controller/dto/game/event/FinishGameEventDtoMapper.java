package pl.wturnieju.controller.dto.game.event;

import org.mapstruct.Mapper;

import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;

@Mapper(componentModel = "spring")
public interface FinishGameEventDtoMapper {

    FinishGameUpdateEvent finishGameEventDtoToFinishGameUpdateEvent(FinishGameEventDto dto);
}
