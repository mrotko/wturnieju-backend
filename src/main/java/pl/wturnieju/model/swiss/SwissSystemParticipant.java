package pl.wturnieju.model.swiss;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SwissSystemParticipant extends SystemParticipant {

    private boolean bye;

    private double points;
}
