package pl.wturnieju.controller.dto.game.event;

import org.mapstruct.Mapper;

import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;

@Mapper(componentModel = "spring")
public interface StartGameEventDtoMapper {

    StartGameUpdateEvent mapStartGameEventDtoToStartGameUpdateEvent(StartGameEventDto dto);
}
