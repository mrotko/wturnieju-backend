package pl.wturnieju;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import lombok.Data;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.generic.GenericProfile;

@Data
public class SwissSystemParticipant extends Persistent {

    private GenericProfile profile;

    private LinkedList<SwissSystemParticipant> opponents = new LinkedList<>();

    private Map<String, Double> opponentsIdsToResultsMap = new HashMap<>();

    private boolean bye;

    private double points;

}
