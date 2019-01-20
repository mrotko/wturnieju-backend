package pl.wturnieju.controller.dto.game.event;

import org.mapstruct.Mapper;

import pl.wturnieju.gameeditor.finish.FinishChessGameUpdateEventImpl;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;

@Mapper(componentModel = "spring")
public interface FinishGameEventDtoMapper {

    default FinishGameUpdateEvent finishGameEventDtoToFinishGameUpdateEvent(FinishGameEventDto dto) {
        switch (dto.getCompetitionType()) {
        case CHESS:
            return finishChessGameEventDtoToFinishChessGameUpdateEvent((FinishChessGameEventDto) dto);
        default:
            return null;
        }
    }

    FinishChessGameUpdateEventImpl finishChessGameEventDtoToFinishChessGameUpdateEvent(FinishChessGameEventDto dto);
}
