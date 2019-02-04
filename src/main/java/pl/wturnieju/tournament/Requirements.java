package pl.wturnieju.tournament;

import java.util.List;

import lombok.Data;

@Data
public class Requirements {

    private int plannedRounds;

    private int minParticipants;

    private int maxParticipants;

    private List<StageType> requiredAllGamesEndedStageTypes;
}
