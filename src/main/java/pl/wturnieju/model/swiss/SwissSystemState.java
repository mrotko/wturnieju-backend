package pl.wturnieju.model.swiss;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Data;
import pl.wturnieju.model.generic.SwissTournamentTable;
import pl.wturnieju.model.generic.TournamentSystemState;

@Data
public class SwissSystemState extends TournamentSystemState {

    private List<SwissSystemParticipant> participants = new ArrayList<>();

    @Override
    public SwissTournamentTable getTournamentTable() {
        return (SwissTournamentTable) super.getTournamentTable();
    }

    @Transient
    public Optional<SwissSystemParticipant> getParticipantById(String id) {
        return participants.stream().filter(p -> p.getProfileId().equals(id)).findFirst();
    }
}