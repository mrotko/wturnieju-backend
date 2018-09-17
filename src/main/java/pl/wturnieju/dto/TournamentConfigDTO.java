package pl.wturnieju.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentSystemType;

public class TournamentConfigDTO {

    private Map<CompetitionType, List<TournamentSystemType>> availableSystems = new HashMap<>();

    public TournamentConfigDTO() {
        initAvailableSystems();
    }

    private void initAvailableSystems() {
        availableSystems.put(CompetitionType.CHESS, Arrays.asList(TournamentSystemType.SWISS));
    }

    public Map<CompetitionType, List<TournamentSystemType>> getAvailableSystems() {
        return availableSystems;
    }
}
