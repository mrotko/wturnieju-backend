package pl.wturnieju.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.schedule.IScheduleEditor;
import pl.wturnieju.schedule.ScheduleEditorFactory;
import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.ITournamentScheduleService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.tournament.Tournament;

@RequiredArgsConstructor
@Service
public class TournamentScheduleService implements ITournamentScheduleService {

    private final ITournamentService tournamentService;

    private final IGameFixtureService gameFixtureService;

    private final ApplicationContext context;

    @Override
    public List<GameFixture> generateSchedule(String tournamentId, String groupId) {
        var editor = createScheduleEditor(tournamentId);
        return editor.generateGames(groupId);
    }

    @Override
    public List<GameFixture> getAllGameFixtures(String tournamentId) {
        return gameFixtureService.getAllByTournamentId(tournamentId);
    }

    @Override
    public List<GameFixture> getGameFixturesBeforeStart(String tournamentId) {
        return getAllGameFixtures(tournamentId).stream()
                .filter(gameFixture -> gameFixture.getGameStatus() == GameStatus.BEFORE_START)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameFixture> getEndedGameFixtures(String tournamentId) {
        return getAllGameFixtures(tournamentId).stream()
                .filter(gameFixture -> gameFixture.getGameStatus() == GameStatus.ENDED)
                .collect(Collectors.toList());
    }

    @Override
    public GameFixture updateGameFixture(String tournamentId, GameFixture gameFixture) {
        var editor = createScheduleEditor(tournamentId);
        return editor.updateGame(gameFixture);
    }

    @Override
    public List<GameFixture> saveSchedule(String tournamentId, List<GameFixture> games) {
        var editor = createScheduleEditor(tournamentId);
        editor.deleteGeneratedGames(games.stream().map(GameFixture::getId).collect(Collectors.toList()));
        return editor.addGames(games);
    }

    @Override
    public List<GameFixture> getGeneratedSchedule(String tournamentId, List<String> gameIds) {
        var editor = createScheduleEditor(tournamentId);
        return editor.getGeneratedGames(gameIds);
    }

    @Override
    public List<GameFixture> getGameFixturesBetweenDates(String tournamentId, Timestamp dateFrom, Timestamp dateTo) {
        return getAllGameFixtures(tournamentId).stream()
                .filter(gameFixture -> gameFixture.getStartDate() != null)
                .filter(gameFixture -> gameFixture.getStartDate().isBetweenIncluded(dateFrom, dateTo))
                .collect(Collectors.toList());
    }

    private IScheduleEditor createScheduleEditor(String tournamentId) {
        return ScheduleEditorFactory.create(context, getTournamentById(tournamentId));
    }

    private Tournament getTournamentById(String tournamentId) {
        return tournamentService.getById(tournamentId);
    }
}
