package pl.wturnieju.inserter;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.service.ITournamentCreatorService;

@RequiredArgsConstructor
public class TournamentInserter {

    private final ITournamentCreatorService tournamentCreatorService;

    public void insertTournamentToDatabase() {
        //        tournamentCreatorService.create(prepareChessTournamentDto());
    }

}
