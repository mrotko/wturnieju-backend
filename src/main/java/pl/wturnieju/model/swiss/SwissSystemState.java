package pl.wturnieju.model.swiss;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.model.generic.SwissTournamentTable;
import pl.wturnieju.model.generic.TournamentSystemState;

@Data
@EqualsAndHashCode(callSuper = true)
public class SwissSystemState extends TournamentSystemState {

    @Override
    public SwissTournamentTable getTournamentTable() {
        return (SwissTournamentTable) super.getTournamentTable();
    }
}