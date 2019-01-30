package pl.wturnieju.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.service.IGameEditorService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.tournament.system.TournamentSystem;
import pl.wturnieju.tournament.system.TournamentSystemFactory;

@Service
@RequiredArgsConstructor
public class GameEditorService implements IGameEditorService {

    private final ITournamentService tournamentService;

    private final ApplicationContext context;

    @Override
    public GameFixture startGame(StartGameUpdateEvent gameUpdateEvent) {
        var tournamentSystem = createTournamentSystem(gameUpdateEvent.getTournamentId());
        return tournamentSystem.startGame(gameUpdateEvent);
    }

    @Override
    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        var tournamentSystem = createTournamentSystem(finishGameUpdateEvent.getTournamentId());
        return tournamentSystem.finishGame(finishGameUpdateEvent);
    }

    private TournamentSystem createTournamentSystem(String tournamentId) {
        var tournament = tournamentService.getTournament(tournamentId);
        return TournamentSystemFactory.create(context, tournament);
    }
}
