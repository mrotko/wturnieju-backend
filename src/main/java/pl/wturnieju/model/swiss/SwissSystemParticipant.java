package pl.wturnieju.model.swiss;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import lombok.Data;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.Persistent;

@Data
public class SwissSystemParticipant extends Persistent {

    private IProfile profile;

    private LinkedList<SwissSystemParticipant> opponents = new LinkedList<>();

    private Map<String, Double> opponentsIdsToResultsMap = new HashMap<>();

    private boolean bye;

    private double points;
}
