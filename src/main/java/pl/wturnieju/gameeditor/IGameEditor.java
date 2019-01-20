package pl.wturnieju.gameeditor;

import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;

public interface IGameEditor {

    GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent);

    GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent);
}
