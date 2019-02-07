package pl.wturnieju.gameeditor;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.exception.UnknownEnumTypeException;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.CompetitionType;

@RequiredArgsConstructor
public class GameEditorFactory {

    private final CompetitionType competitionType;

    public IGameEditor createGameEditor(GameFixture gameFixture) {
        switch (competitionType) {
        case CHESS:
            return new ChessGameEditor(gameFixture);
        case FOOTBALL:
            return new FootballGameEditor(gameFixture);
        case TENNIS:
            return new TennisGameEditor(gameFixture);
        case CUSTOM:
        default:
            throw new UnknownEnumTypeException(competitionType);
        }
    }
}
