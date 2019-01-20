package pl.wturnieju.gameeditor;


import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;

@RequiredArgsConstructor
public abstract class GameEditor implements IGameEditor {

    protected final GameFixture gameFixture;
}
