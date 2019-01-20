package pl.wturnieju.controller.dto.game.event;

import org.mapstruct.Mapper;

import pl.wturnieju.gameeditor.start.StartChessGameUpdateEventImpl;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;

@Mapper(componentModel = "spring")
public interface StartGameEventDtoMapper {

    default StartGameUpdateEvent mapStartGameEventDtoToStartGameUpdateEvent(StartGameEventDto dto) {

        switch (dto.getCompetitionType()) {
        case CHESS:
            return mapStartChessGameEventDtoToStartGameUpdateEvent((StartChessGameEventDto) dto);
        default:
            return null;
        }

    }

    StartChessGameUpdateEventImpl mapStartChessGameEventDtoToStartGameUpdateEvent(StartChessGameEventDto chessGameEventDto);

}
