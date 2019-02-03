package pl.wturnieju.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.schedule.IScheduleEditor;
import pl.wturnieju.schedule.ScheduleEditorFactory;
import pl.wturnieju.service.ITournamentScheduleService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.tournament.system.TournamentSystem;
import pl.wturnieju.tournament.system.TournamentSystemFactory;
import pl.wturnieju.tournament.system.state.SystemState;

@RequiredArgsConstructor
@Service
public class TournamentScheduleService implements ITournamentScheduleService {

    private final ITournamentService tournamentService;

    private final ApplicationContext context;

    @Override
    public List<GameFixture> generateSchedule(String tournamentId) {
        var editor = createScheduleEditor(tournamentId);
        return editor.generateGames();
    }

    @Override
    public List<GameFixture> getGameFixtures(String tournamentId) {
        var state = createTournamentSystem(tournamentId).getSystemState();
        return Optional.ofNullable(state).map(SystemState::getGameFixtures).orElse(Collections.emptyList());
    }

    @Override
    public List<GameFixture> getGameFixturesBeforeStart(String tournamentId) {
        return getGameFixtures(tournamentId).stream()
                .filter(gameFixture -> gameFixture.getGameStatus() == GameStatus.BEFORE_START)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameFixture> getEndedGameFixtures(String tournamentId) {
        return getGameFixtures(tournamentId).stream()
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
        return getGameFixtures(tournamentId).stream()
                .filter(gameFixture -> gameFixture.getStartDate() != null)
                .filter(gameFixture -> gameFixture.getStartDate().isBetweenIncluded(dateFrom, dateTo))
                .collect(Collectors.toList());
    }

    private IScheduleEditor createScheduleEditor(String tournamentId) {
        var system = createTournamentSystem(tournamentId);
        var scheduleEditor = ScheduleEditorFactory.create(system);
        return scheduleEditor;
    }

    private TournamentSystem createTournamentSystem(String tournamentId) {
        var tournament = tournamentService.getTournament(tournamentId);
        return TournamentSystemFactory.create(context, tournament);
    }
}
