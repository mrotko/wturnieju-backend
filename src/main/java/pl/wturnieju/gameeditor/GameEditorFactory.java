package pl.wturnieju.gameeditor;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gameeditor.finish.FinishChessGameUpdateEventImpl;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartChessGameUpdateEventImpl;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.CompetitionType;

@RequiredArgsConstructor
public class GameEditorFactory {

    private final CompetitionType competitionType;

    public FinishGameUpdateEvent createFinishGameUpdateEvent() {
        switch (competitionType) {
        case CHESS:
            new FinishChessGameUpdateEventImpl();
        default:
            throw new IllegalArgumentException();
        }
    }

    public StartGameUpdateEvent createStartGameUpdateEvent() {
        switch (competitionType) {
        case CHESS:
            new StartChessGameUpdateEventImpl();
        default:
            throw new IllegalArgumentException();
        }
    }

    public IGameEditor createGameEditor(GameFixture gameFixture) {
        switch (competitionType) {
        case CHESS:
            return new ChessGameEditor(gameFixture);
        default:
            throw new IllegalArgumentException();
        }
    }
}
