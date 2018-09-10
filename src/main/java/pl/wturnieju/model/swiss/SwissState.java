package pl.wturnieju.model.swiss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import pl.wturnieju.model.Duel;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentState;

@Getter
public class SwissState extends TournamentState {

    private List<SwissSystemParticipant> participants = new ArrayList<>();

    private Map<Integer, List<Duel>> roundToDuelsMap = new HashMap<>();

    public SwissState(Tournament tournament) {
        super(tournament);
    }

}