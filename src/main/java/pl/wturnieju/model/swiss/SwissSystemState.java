package pl.wturnieju.model.swiss;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import pl.wturnieju.model.generic.SwissTournamentTable;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentState;

@Data
public class SwissSystemState extends TournamentSystemState {

    private List<SwissSystemParticipant> participants = new ArrayList<>();

    @Override
    public SwissTournamentTable getTournamentTable() {
        return (SwissTournamentTable) super.getTournamentTable();
    }
}