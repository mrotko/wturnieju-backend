package pl.wturnieju.model.swiss;

import lombok.Data;
import pl.wturnieju.model.generic.SwissTournamentTable;
import pl.wturnieju.model.generic.TournamentSystemState;

@Data
public class SwissSystemState extends TournamentSystemState {

    @Override
    public SwissTournamentTable getTournamentTable() {
        return (SwissTournamentTable) super.getTournamentTable();
    }
}