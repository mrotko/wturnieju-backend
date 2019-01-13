package pl.wturnieju.tournament;

import lombok.Data;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.InvitationStatus;

@Data
public class Participant implements IProfile {

    private String id;

    private ParticipantStatus participantStatus;

    private InvitationStatus invitationStatus;

    private String name;
}
