package pl.wturnieju.model.swiss;

import lombok.Data;

@Data
public class RoundRobinSystemParticipant extends SystemParticipant {

    private double points;

    boolean bye;
}
