package pl.wturnieju.handler;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentSystemState;
import pl.wturnieju.model.swiss.SystemParticipant;


@Getter
@Setter
@RequiredArgsConstructor
public abstract class TournamentSystem<T extends TournamentSystemState> implements
        ITournamentSystem {

    protected Tournament tournament;

    protected T state;

    protected Optional<SystemParticipant> getParticipantById(String profileId) {
        return state.getParticipants().stream()
                .filter(p -> p.getProfileId().equals(profileId))
                .findFirst();
    }

    protected Optional<Fixture> findFixture(String fixtureId) {
        return getState().getFixtures().stream()
                .filter(f -> f.getId().equals(fixtureId))
                .findFirst();
    }

    private Optional<Fixture> findFixture(GenericFixtureUpdateBundle bundle) {
        return findFixture(bundle.getFixtureId());
    }

    public abstract List<Fixture> prepareNextRound();

    public abstract void createNextRoundFixtures(List<Fixture> fixtures);

    protected void addOpponentsToParticipants(List<Fixture> fixtures) {
        fixtures.forEach(fixture -> {
            var firstPlayer = getParticipantById(fixture.getPlayersIds().left);
            var secondPlayer = getParticipantById(fixture.getPlayersIds().right);

            firstPlayer.ifPresent(
                    p -> p.getOpponentsIds().add(secondPlayer.map(SystemParticipant::getProfileId).orElse(null)));
            secondPlayer.ifPresent(
                    p -> p.getOpponentsIds().add(firstPlayer.map(SystemParticipant::getProfileId).orElse(null)));
        });
    }
}
