package pl.wturnieju.schedule;

import java.util.Objects;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.tournament.system.state.SystemState;

public abstract class ScheduleEditor<T extends GameFixture> implements IScheduleEditor<T> {

    protected T getGameFixture(SystemState<T> state, String gameId) {
        return state.getGameFixtures().stream()
                .filter(g -> Objects.equals(g.getId(), gameId))
                .findFirst()
                .orElse(null);
    }
}
