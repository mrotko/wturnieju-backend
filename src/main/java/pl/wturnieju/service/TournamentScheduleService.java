package pl.wturnieju.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.schedule.IScheduleEditor;
import pl.wturnieju.schedule.ScheduleEditorFactory;
import pl.wturnieju.tournament.system.TournamentSystemFactory;

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

    @SuppressWarnings("unchecked")
    private IScheduleEditor<GameFixture> createScheduleEditor(String tournamentId) {
        var tournament = tournamentService.getTournament(tournamentId);
        var system = TournamentSystemFactory.create(context, tournament);
        var scheduleEditor = ScheduleEditorFactory.create(system);
        return (IScheduleEditor<GameFixture>) scheduleEditor;
    }
}
