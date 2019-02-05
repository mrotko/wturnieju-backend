package pl.wturnieju.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.service.ITournamentPresentationService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.ITournamentTableService;
import pl.wturnieju.tournament.system.TournamentSystemFactory;
import pl.wturnieju.tournament.system.table.TournamentTable;

@RequiredArgsConstructor
@Service
public class TournamentPresentationService implements ITournamentPresentationService {

    private final ITournamentTableService tournamentTableService;

    private final ITournamentService tournamentService;

    private final ApplicationContext context;

    @Override
    public TournamentTable retrieveTournamentTable(String tournamentId, String groupId) {
        var cachedTable = tournamentTableService.getByGroupId(groupId);
        if (cachedTable != null) {
            return cachedTable;
        }
        var tournament = tournamentService.getById(tournamentId);
        var system = TournamentSystemFactory.create(context, tournament);
        var table = system.buildTournamentTable(groupId);
        tournamentTableService.insert(table);
        return table;
    }
}
