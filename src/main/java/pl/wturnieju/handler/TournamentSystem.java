package pl.wturnieju.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.FixtureBuilderFactory;
import pl.wturnieju.model.FixtureStatus;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentSystemState;
import pl.wturnieju.model.generic.TournamentTableRow;
import pl.wturnieju.model.swiss.SystemParticipant;


@Data
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

    protected Map<String, List<String>> groupPlayersWithNotPlayed() {
        var allParticipantsIds = getState().getParticipants().stream()
                .map(SystemParticipant::getProfileId)
                .collect(Collectors.toList());

        return getState().getParticipants().stream()
                .map(participant -> {
                    var playersNotPlayedIds = new ArrayList<>(allParticipantsIds);
                    playersNotPlayedIds.remove(participant.getProfileId());
                    playersNotPlayedIds.removeAll(participant.getOpponentsIds());
                    return MutablePair.of(participant.getProfileId(), playersNotPlayedIds);
                })
                .collect(Collectors.toMap(pair -> pair.left, pair -> pair.right));
    }

    protected List<Fixture> generateFixtures(Map<String, String> pairedPlayers) {
        Timestamp timestamp = Timestamp.now();
        return pairedPlayers.entrySet().stream()
                .map(pair -> FixtureBuilderFactory.getInstance(tournament.getCompetitionType()).builder().firstPlayer(
                        pair.getKey()).secondPlayer(pair.getValue()).build())
                .peek(fixture -> {
                    fixture.setTimestamp(timestamp);
                    fixture.setResult(new MutablePair<>(null, null));
                    fixture.setRound(getState().getCurrentRound() + 1);
                    fixture.setStatus(FixtureStatus.BEFORE_START);
                })
                .collect(Collectors.toList());
    }

    public abstract List<Fixture> prepareNextRound();

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

    protected <R extends TournamentTableRow<R>> Optional<R> getTournamentRow(String profileId) {
        return state.getTournamentTable().getProfileRow(profileId);
    }

    public void createNextRoundFixtures(List<Fixture> fixtures) {
        fixtures.stream()
                .filter(fixture -> fixture.getPlayersIds().getRight() == null)
                .findFirst()
                .ifPresent(fixture -> {
                    fixture.setDirty(true);
                    fixture.setStatus(FixtureStatus.ENDED);
                    fixture.setWinnerId(fixture.getPlayersIds().getLeft());
                });


        state.getFixtures().addAll(fixtures);
        state.setCurrentRound(state.getCurrentRound() + 1);
        addOpponentsToParticipants(fixtures);
        updateTable();
    }

    protected abstract void updateTable();
}
