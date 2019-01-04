package pl.wturnieju.model.swiss;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoundRobinSystemParticipant extends SystemParticipant {

    private double points;

    boolean bye;
}
