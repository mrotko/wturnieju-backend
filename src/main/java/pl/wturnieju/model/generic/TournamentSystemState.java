package pl.wturnieju.model.generic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.service.GenericTournamentUpdateBundle;


@RequiredArgsConstructor
@Data
public abstract class TournamentSystemState extends Persistent {

    private LocalDateTime lastUpdate;

    private int currentRound;

    private GenericTournamentTable tournamentTable;

    private Map<Integer, List<Duel>> roundToDuelsMap = new HashMap<>();
}
