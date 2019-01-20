package pl.wturnieju.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.controller.dto.game.event.FinishGameEventDto;
import pl.wturnieju.controller.dto.game.event.StartGameEventDto;
import pl.wturnieju.controller.dto.game.event.StartGameEventDtoMapper;
import pl.wturnieju.controller.dto.tournament.gamefixture.GameFixtureDto;
import pl.wturnieju.controller.dto.tournament.gamefixture.GameFixtureDtoMapper;
import pl.wturnieju.service.IGameEditorService;
import pl.wturnieju.service.ITournamentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("game-editor")
public class GameEditorController {

    private final DtoMappers mappers;

    private final ITournamentService tournamentService;

    private final IGameEditorService gameEditorService;

    private final StartGameEventDtoMapper startGameEventDtoMapper;

    private final GameFixtureDtoMapper gameFixtureDtoMapper;


    @PatchMapping("start-game")
    public GameFixtureDto startGame(@RequestBody StartGameEventDto dto) {
        var update = startGameEventDtoMapper.mapStartGameEventDtoToStartGameUpdateEvent(dto);
        var game = gameEditorService.startGame(update);
        var tournament = tournamentService.getTournament(dto.getTournamentId());
        return gameFixtureDtoMapper.gameFixtureToGameFixtureDto(game, tournament);
    }

    @PatchMapping("finish-game")
    public GameFixtureDto finishGame(@RequestBody FinishGameEventDto dto) {

        return new GameFixtureDto();
    }
}
