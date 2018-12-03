package pl.wturnieju.model.swiss;

import lombok.Data;

@Data
public class SwissSystemParticipant extends SystemParticipant {

    private boolean bye;

    private double points;
}
