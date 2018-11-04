package pl.wturnieju.model.generic;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.Duel;
import pl.wturnieju.model.Persistent;


@RequiredArgsConstructor
@Data
public abstract class TournamentState extends Persistent {

    private final Tournament tournament;

    private LocalDateTime lastUpdate;

    private int currentRound;

    private GenericTournamentTable tournamentTable;

    private Map<Integer, List<Duel>> roundToDuelsMap = new HashMap<>();
}
