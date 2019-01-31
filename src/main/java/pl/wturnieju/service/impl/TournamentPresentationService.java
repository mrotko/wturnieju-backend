package pl.wturnieju.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.service.ITournamentPresentationService;
import pl.wturnieju.tournament.system.TournamentSystem;
import pl.wturnieju.tournament.system.TournamentSystemFactory;
import pl.wturnieju.tournament.system.table.TournamentTable;

@RequiredArgsConstructor
@Service
public class TournamentPresentationService implements ITournamentPresentationService {

    private final TournamentService tournamentService;

    private final ApplicationContext context;

    @Override
    public TournamentTable getTournamentTable(String tournamentId) {
        var tournament = tournamentService.getTournament(tournamentId);
        var system = (TournamentSystem<?>) TournamentSystemFactory.create(context, tournament);
        return system.buildTournamentTable();
    }
}
