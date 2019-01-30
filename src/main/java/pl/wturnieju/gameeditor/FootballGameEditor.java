package pl.wturnieju.gameeditor;

import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;

public class FootballGameEditor extends GameEditor {

    public FootballGameEditor(GameFixture gameFixture) {
        super(gameFixture);
    }

    @Override
    public GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent) {
        return null;
    }

    @Override
    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        return null;
    }
}
