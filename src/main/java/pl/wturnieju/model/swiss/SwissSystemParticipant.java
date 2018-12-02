package pl.wturnieju.model.swiss;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import lombok.Data;

@Data
public class SwissSystemParticipant {

    private String profileId;

    private LinkedList<String> opponentsIds = new LinkedList<>();

    private Map<String, Double> opponentsIdsToResultsMap = new HashMap<>();

    private boolean bye;

    private double points;
}
