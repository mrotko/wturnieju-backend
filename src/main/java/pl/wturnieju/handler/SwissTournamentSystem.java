package pl.wturnieju.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.FixtureBuilderFactory;
import pl.wturnieju.model.FixtureStatus;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentStateFactory;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.model.generic.ResultBundleUpdateContent;
import pl.wturnieju.model.generic.StatusBundleUpdateContent;
import pl.wturnieju.model.generic.SwissFixtureUpdateBundle;
import pl.wturnieju.model.swiss.SwissSystemParticipant;
import pl.wturnieju.model.swiss.SwissSystemState;
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

    private List<Fixture> generateFixtures(List<Pair<String, String>> pairedPlayers) {
        Timestamp timestamp = Timestamp.now();
        return pairedPlayers.stream()
                .map(pair -> FixtureBuilderFactory.getInstance(tournament.getCompetitionType()).builder().firstPlayer(
                        pair.getLeft()).secondPlayer(pair.getRight()).build())
                .peek(fixture -> {
                    fixture.setTimestamp(timestamp);
                    fixture.setPoints(new MutablePair<>(0D, 0D));
                    fixture.setGameSeries(getState().getCurrentRound() + 1);
                    fixture.setStatus(FixtureStatus.BEFORE_START);
                })
                .collect(Collectors.toList());
    }

    private List<Pair<String, String>> pairPlayersBySameScore(Map<String, List<String>> playerToNotPlayedPlayersIds) {
        List<Pair<String, String>> pairs = new ArrayList<>();

        Set<String> availablePlayersIds = new HashSet<>(playerToNotPlayedPlayersIds.keySet());

        playerToNotPlayedPlayersIds.forEach((participantId, opponentsIds) -> {
            if (availablePlayersIds.contains(participantId)) {
                getParticipantById(participantId).ifPresent(participant -> {
                    var opponent = opponentsIds.stream()
                            .filter(availablePlayersIds::contains)
                            .map(this::getParticipantById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .filter(o -> o.getPoints() == participant.getPoints())
                            .findFirst();
                    opponent.map(SwissSystemParticipant::getProfileId).ifPresent(opponentId -> {
                        pairs.add(new MutablePair<>(participantId, opponent.get().getProfileId()));
                        availablePlayersIds.remove(participantId);
                        availablePlayersIds.remove(opponentId);
                    });
                });
            }
        });
        return pairs;
    }

    private Map<String, List<String>> groupPlayersWithNotPlayed() {
        Map<String, List<String>> playerToNotPlayedPlayersIds = new HashMap<>();

        Map<String, Boolean> allPlayersToPlayedMap = getState().getParticipants().stream()
                .collect(Collectors.toMap(SwissSystemParticipant::getProfileId, p -> false));

        getState().getParticipants().forEach(participant -> {
            participant.getOpponents().forEach(
                    opponent -> allPlayersToPlayedMap.put(opponent.getProfileId(), true));
            playerToNotPlayedPlayersIds.put(participant.getProfileId(), allPlayersToPlayedMap.entrySet().stream()
                    .filter(entry -> !entry.getValue())
                    .filter(entry -> !participant.getProfileId().equals(entry.getKey()))
                    .map(Entry::getKey)
                    .collect(Collectors.toList())
            );
            allPlayersToPlayedMap.replaceAll((key, value) -> false);
        });
        return playerToNotPlayedPlayersIds;
    }

    private Optional<SwissSystemParticipant> getParticipantById(String profileId) {
        return getState().getParticipants().stream()
                .filter(p -> p.getProfileId().equals(profileId))
                .findFirst();
    }

    private void handleTournamentPause(PauseTournamentBundleUpdateContent content) {
        // TODO(mr): 13.11.2018 impl pause but if it's necessary??
    }

    private void handleTournamentStart(StartTournamentBundleUpdateContent content) {
        // TODO(mr): 13.11.2018 alert triggered by tournament start
        tournament.setStartDate(content.getStartDate());
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournament.setTournamentSystemState(TournamentStateFactory.getInstance(tournament));
    }

    private Optional<Fixture> findFixture(SwissFixtureUpdateBundle bundle) {
        return getState().getFixtures().stream()
                .filter(f -> f.getId().equals(bundle.getFixtureId()))
                .findFirst();
    }

    private Optional<Fixture> findFixture(String fixtureId) {
        return getState().getFixtures().stream()
                .filter(f -> f.getId().equals(fixtureId))
                .findFirst();
    }


    private void updateStatus(StatusBundleUpdateContent content, String fixtureId) {
        // TODO(mr): 10.11.2018 impl aktualizacja statusu spotkania
        //        findFixture(fixtureId).
    }

    private void updateResult(ResultBundleUpdateContent content, String fixtureId) {


        // TODO(mr): 11.11.2018 impl aktualizacja wyniku spotkania
        //        if (content != null && fixtureId != null) {
        //            fixture.setPoints(content.getNewResult().getPoints());
        //        }
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
    public void addNextRoundFixtures(List<Fixture> fixtures) {
        state.getFixtures().addAll(fixtures);
    }
}
