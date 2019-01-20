package pl.wturnieju.service;

import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;

public interface IGameEditorService {

    GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent);

    GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent);

}
