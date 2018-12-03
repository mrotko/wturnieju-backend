package pl.wturnieju.model.swiss;

import java.util.LinkedList;

import lombok.Data;

@Data
public abstract class SystemParticipant {

    private LinkedList<String> opponentsIds = new LinkedList<>();

    private String profileId;
}
