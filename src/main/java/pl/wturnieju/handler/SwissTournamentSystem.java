package pl.wturnieju.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;

import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.FixtureBuilderFactory;
import pl.wturnieju.model.FixtureStatus;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentParticipant;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.TournamentTableFactory;
import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.model.generic.ResultBundleUpdateContent;
import pl.wturnieju.model.generic.StatusBundleUpdateContent;
import pl.wturnieju.model.generic.SwissFixtureUpdateBundle;
import pl.wturnieju.model.generic.SwissTournamentTableRow;
import pl.wturnieju.model.generic.TournamentTableRow;
import pl.wturnieju.model.swiss.SwissSystemParticipant;
import pl.wturnieju.model.swiss.SwissSystemState;
import pl.wturnieju.model.swiss.SystemParticipant;
import pl.wturnieju.service.EndTournamentBundleUpdateContent;
import pl.wturnieju.service.GenericTournamentUpdateBundle;
import pl.wturnieju.service.NextRoundTournamentBundleUpdateContent;
import pl.wturnieju.service.PauseTournamentBundleUpdateContent;
import pl.wturnieju.service.StartTournamentBundleUpdateContent;
import pl.wturnieju.service.SwissTournamentUpdateBundle;


// TODO(mr): 13.11.2018 after each update should be save in db

public class SwissTournamentSystem extends TournamentSystem<SwissSystemState> {

    // TODO(mr): 13.11.2018 better method should be generic, change in future, add somee handle for bundle
    private void dispatchFixtureUpdateBundle(SwissFixtureUpdateBundle bundle) {
        // TODO(mr): 11.11.2018 impl przyjmowanie paczek z aktualizacjami
        switch (bundle.getContent().getType()) {
        case STATUS:
            updateStatus((StatusBundleUpdateContent) bundle.getContent(), bundle.getFixtureId());
            break;
        case RESULT:
            updateResult((ResultBundleUpdateContent) bundle.getContent(), bundle.getFixtureId());
            break;
        default:
            break;
        }
        getState().getUpdateFixtureBundles().add(bundle);
    }

    // TODO(mr): 13.11.2018 better method should be generic, change in future, add some handle for bundle
    private void dispatchTournamentUpdateBundle(SwissTournamentUpdateBundle bundle) {

        switch (bundle.getContent().getType()) {
        case START:
            handleTournamentStart((StartTournamentBundleUpdateContent) bundle.getContent());
            /*
             * na froncie start turnieju pojawia się data tak, żeby wybrać albo aktualną albo planowaną, od tego czasu odlicza sie czas trwania
             * backend nie interesuje wybieranie, odbierana jest wybrana data
             * trzeba przygotować jakąś odpowiedź, można założyć, że wyłanie ok daje prawo do wysłania kolejnych potrzebnych żądań
             * wszystkie aktualizacje muszą być zabepiecznone przed próbą użycia ich przez osoby nieuprawnione
             * można uaktualić spotkania, nie można zmienić uczestników (można wyrzucać ale nie w tej wersji), tak samo z pomocnikami (w dodatku ich nie będzie ;))
             *
             * */

            break;
        case PAUSE:
            handleTournamentPause((PauseTournamentBundleUpdateContent) bundle.getContent());
            break;
        case NEXT_ROUND:
            handleTournamentNextRound((NextRoundTournamentBundleUpdateContent) bundle.getContent());
            break;
        case END:
            handleTournamentEnd((EndTournamentBundleUpdateContent) bundle.getContent());
            break;
        default:
            // TODO(mr): 13.11.2018 impl maybe some exception??
            break;
        }
    }

    private void handleTournamentEnd(EndTournamentBundleUpdateContent content) {
        // TODO(mr): 13.11.2018 alert triggered by tournament end
        tournament.setStatus(TournamentStatus.ENDED);
        tournament.setEndDate(content.getEndDate());
    }

    // TODO(mr): 13.11.2018 test important to test this function
    private void handleTournamentNextRound(NextRoundTournamentBundleUpdateContent content) {

    }

    private List<Fixture> generateFixtures(Map<String, String> pairedPlayers) {
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

    private Map<String, String> pairPlayersBySameScore(Map<String, List<String>> playerToNotPlayedPlayersIds) {
        Map<String, String> pairs = new HashMap<>();

        Set<String> availablePlayersIds = new HashSet<>(playerToNotPlayedPlayersIds.keySet());

        playerToNotPlayedPlayersIds.forEach((participantId, opponentsIds) -> {
            if (availablePlayersIds.contains(participantId)) {
                var participant = (SwissSystemParticipant) getParticipantById(participantId).orElseThrow();
                var opponent = opponentsIds.stream()
                        .filter(availablePlayersIds::contains)
                        .map(this::getParticipantById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(p -> (SwissSystemParticipant) p)
                        .filter(o -> o.getPoints() == participant.getPoints())
                        .findFirst();

                opponent.map(SystemParticipant::getProfileId).ifPresent(opponentId -> {
                    pairs.put(participantId, opponentId);
                    availablePlayersIds.remove(participantId);
                    availablePlayersIds.remove(opponentId);
                });
            }
        });

        int pivot = availablePlayersIds.size() / 2;
        var availablePlayersIdsList = new ArrayList<>(availablePlayersIds);


        for (int i = 0; i < pivot; i++) {
            pairs.put(availablePlayersIdsList.get(i), availablePlayersIdsList.get(pivot + i));
            availablePlayersIds.remove(availablePlayersIdsList.get(i));
            availablePlayersIds.remove(availablePlayersIdsList.get(pivot + i));
        }

        if (!availablePlayersIds.isEmpty()) {
            pairs.put(availablePlayersIds.iterator().next(), null);
        }

        return pairs;
    }

    private Map<String, List<String>> groupPlayersWithNotPlayed() {
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

    private void handleTournamentPause(PauseTournamentBundleUpdateContent content) {
        // TODO(mr): 13.11.2018 impl pause but if it's necessary??
    }

    private void handleTournamentStart(StartTournamentBundleUpdateContent content) {
        // TODO(mr): 13.11.2018 alert triggered by tournament start
        tournament.setStartDate(content.getStartDate());
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        state.setParticipants(tournament.getParticipants().stream()
                .map(this::createSwissSystemParticipant)
                .collect(Collectors.toList()));
        state.setTournamentTable(TournamentTableFactory.getTable(tournament));
    }

    private SwissSystemParticipant createSwissSystemParticipant(TournamentParticipant participant) {
        var swissParticipant = new SwissSystemParticipant();

        swissParticipant.setBye(false);
        swissParticipant.setOpponentsIds(new LinkedList<>());
        swissParticipant.setPoints(0.);
        swissParticipant.setProfileId(participant.getId());

        return swissParticipant;
    }

    private void updateStatus(StatusBundleUpdateContent content, String fixtureId) {
        // TODO(mr): 10.11.2018 impl aktualizacja statusu spotkania
        //        findFixture(fixtureId).
    }

    private void updateResult(ResultBundleUpdateContent content, String fixtureId) {
        findFixture(fixtureId).ifPresent(fixture -> {
            fixture.setResult(content.getNewResult().getResult());

            if (!fixture.getResult().getLeft().equals(fixture.getResult().getRight())) {
                if (fixture.getResult().getLeft() > fixture.getResult().getRight()) {
                    fixture.setStatus(FixtureStatus.ENDED);
                    fixture.setWinnerId(fixture.getPlayersIds().getLeft());
                } else {
                    fixture.setStatus(FixtureStatus.ENDED);
                    fixture.setWinnerId(fixture.getPlayersIds().getRight());
                }
            } else {
                fixture.setStatus(FixtureStatus.ENDED);
                fixture.setWinnerId(null);
            }

            fixture.setDirty(true);
            updateTable();
        });
    }

    private void updateTable() {
        state.getFixtures().stream()
                .filter(Fixture::isDirty)
                .forEach(fixture -> {
                    var firstPlayerId = fixture.getPlayersIds().getLeft();
                    var secondPlayerId = fixture.getPlayersIds().getRight();
                    var winnerId = fixture.getWinnerId();

                    var firstPlayerRow = getState().getTournamentTable().getProfileRow(firstPlayerId).orElseThrow();
                    var secondPlayerRow = getState().getTournamentTable().getProfileRow(secondPlayerId);

                    if (winnerId != null) {
                        if (firstPlayerId.equals(winnerId)) {
                            updateRowAsWin(firstPlayerRow,
                                    secondPlayerRow.map(TournamentTableRow::getPoints).orElse(0.0));
                            secondPlayerRow.ifPresent(this::updateRowAsLose);
                        } else {
                            updateRowAsWin(secondPlayerRow.orElseThrow(), firstPlayerRow.getPoints());
                            updateRowAsLose(firstPlayerRow);
                        }
                    } else {
                        updateRowAsDraw(firstPlayerRow,
                                secondPlayerRow.map(TournamentTableRow::getPoints).orElseThrow());
                        updateRowAsDraw(secondPlayerRow.orElseThrow(), firstPlayerRow.getPoints());
                    }

                    getState().getTournamentTable().setLastUpdate(Timestamp.now());
                    fixture.setDirty(false);
                });
    }


    private void updateRowAsWin(SwissTournamentTableRow row, double smallPoints) {
        row.setSmallPoints(row.getSmallPoints() + smallPoints);
        row.setPoints(row.getPoints() + 1);
        row.setWins(row.getWins() + 1);
    }

    private void updateRowAsDraw(SwissTournamentTableRow row, double smallPoints) {
        row.setSmallPoints(row.getSmallPoints() + smallPoints);
        row.setPoints(row.getPoints() + 0.5);
        row.setDraws(row.getDraws() + 1);
    }

    private void updateRowAsLose(SwissTournamentTableRow row) {
        row.setLoses(row.getLoses() + 1);
    }

    @Override
    public void updateTournament(GenericTournamentUpdateBundle bundle) {
        dispatchTournamentUpdateBundle((SwissTournamentUpdateBundle) bundle);
        getState().getUpdateTournamentBundles().add(bundle);
    }

    @Override
    public void updateFixture(GenericFixtureUpdateBundle bundle) {
        dispatchFixtureUpdateBundle((SwissFixtureUpdateBundle) bundle);
        getState().getUpdateFixtureBundles().add(bundle);
    }

    @Override
    public List<Fixture> prepareNextRound() {
        if (tournament.getPlannedRounds() <= state.getCurrentRound()) {
            throw new IllegalArgumentException(
                    String.format("declared %s but you want to create more rounds", tournament.getPlannedRounds()));
        }

        var playerToNotPlayedPlayers = groupPlayersWithNotPlayed();
        var pairedPlayers = pairPlayersBySameScore(playerToNotPlayedPlayers);

        // TODO(mr): 13.11.2018 should be more complex
        return generateFixtures(pairedPlayers);
    }

    @Override
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
}
