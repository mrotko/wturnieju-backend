package pl.wturnieju.tournament.system;

import lombok.Data;
import pl.wturnieju.tournament.StageType;

@Data
public class Stage {

    private StageType stageType;

    private Stage next;
}
