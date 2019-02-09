package pl.wturnieju.tournament.system.state;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.PositionOrderElementType;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.tournament.StageType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Group extends Persistent {

    private String name;

    private String tournamentId;

    private List<String> participantIds;

    private StageType stageType;

    private List<PositionOrderElementType> positionOrder;

    private boolean requiredAllGamesEnded;
}
