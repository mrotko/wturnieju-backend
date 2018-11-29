package pl.wturnieju.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.FixtureBuilderFactory;
import pl.wturnieju.model.FixtureStatus;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.TournamentTableFactory;
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


        // TODO(mr): 13.11.2018 check if number is higher than declared
        int nextRoundNumber = getState().getCurrentRoundNumber() + 1;

        // TODO(mr): 13.11.2018 each player should have players which can play

        var playerToNotPlayedPlayers = groupPlayersWithNotPlayed();

        var pairedPlayers = pairPlayersBySameScore(playerToNotPlayedPlayers);

        // TODO(mr): 13.11.2018 should be more complex

        getState().setCurrentRoundNumber(nextRoundNumber);

        getState().getFixtures().addAll(generateFixtures(pairedPlayers));
    }

    private Collection<? extends Fixture> generateFixtures(List<Pair<IProfile, IProfile>> pairedPlayers) {
        LocalDateTime timestamp = LocalDateTime.now();
        return pairedPlayers.stream()
                .map(pair -> FixtureBuilderFactory.getInstance(tournament.getCompetitionType()).builder().firstPlayer(
                        pair.getLeft()).secondPlayer(pair.getRight()).build())
                .peek(fixture -> {
                    fixture.setTimestamp(timestamp);
                    fixture.setPoints(new MutablePair<>(0D, 0D));
                    fixture.setGameSeries(getState().getCurrentRoundNumber());
                    fixture.setStatus(FixtureStatus.BEFORE_START);
                })
                .collect(Collectors.toList());
    }

    private List<Pair<IProfile, IProfile>> pairPlayersBySameScore(Map<IProfile, List<IProfile>> playerToNotPlayedPlayers) {
        List<IProfile> chosenPlayers = new ArrayList<>();
        List<Pair<IProfile, IProfile>> pairs = new ArrayList<>();

        playerToNotPlayedPlayers.forEach((k, v) -> {
            var participant = getParticipantByProfile(k);
            participant.ifPresent(p -> {
                var opponent = v.stream()
                        .map(this::getParticipantByProfile)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(o -> o.getPoints() == p.getPoints())
                        .findFirst();
                if (opponent.isPresent()) {
                    pairs.add(new MutablePair<>(participant.get().getProfile(), opponent.get().getProfile()));
                    chosenPlayers.add(participant.get().getProfile());
                    chosenPlayers.add(opponent.get().getProfile());
                }
            });
        });

        chosenPlayers.forEach(playerToNotPlayedPlayers::remove);
        return pairs;
    }

    private Map<IProfile, List<IProfile>> groupPlayersWithNotPlayed() {
        Map<IProfile, List<IProfile>> playerToNotPlayedPlayers = new HashMap<>();

        Map<IProfile, Boolean> allPlayersToPlayedMap = new HashMap<>();

        getState().getParticipants().forEach(p -> allPlayersToPlayedMap.put(p.getProfile(), false));

        getState().getParticipants().forEach(participant -> {
            allPlayersToPlayedMap.replaceAll((key, value) -> false);
            participant.getOpponents().forEach(opponent -> allPlayersToPlayedMap.put(opponent.getProfile(), true));
            playerToNotPlayedPlayers.put(participant.getProfile(), allPlayersToPlayedMap.entrySet().stream()
                    .filter(entry -> !entry.getValue())
                    .map(Entry::getKey)
                    .collect(Collectors.toList())
            );
        });
        return playerToNotPlayedPlayers;
    }

    private Optional<SwissSystemParticipant> getParticipantByProfile(IProfile profile) {
        return getState().getParticipants().stream()
                .filter(p -> p.getProfile().equals(profile))
                .findFirst();
    }

    private void handleTournamentPause(PauseTournamentBundleUpdateContent content) {
        // TODO(mr): 13.11.2018 impl pause but if it's necessary??
    }

    private void handleTournamentStart(StartTournamentBundleUpdateContent content) {
        // TODO(mr): 13.11.2018 alert triggered by tournament start
        tournament.setStartDate(content.getStartDate());
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        // TODO(mr): 21.11.2018 test if is created table after start
        tournament.getTournamentSystemState().setTournamentTable(TournamentTableFactory.getTable(tournament));
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
}
