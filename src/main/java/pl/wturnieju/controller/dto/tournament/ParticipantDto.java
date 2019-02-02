package pl.wturnieju.controller.dto.tournament;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.model.ParticipantType;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.ParticipantStatus;

@Getter
@Setter
public class ParticipantDto {

    private String id;

    private ParticipantStatus participantStatus;

    private InvitationStatus invitationStatus;

    private ParticipantType participantType;

    private String name;

    private String shortName;

    private List<Participant> members = new ArrayList<>();
}
