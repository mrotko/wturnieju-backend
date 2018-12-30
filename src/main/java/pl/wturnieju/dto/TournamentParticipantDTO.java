package pl.wturnieju.dto;

import lombok.Data;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.model.ParticipantStatus;

@Data
public class TournamentParticipantDTO {

    private String id;

    private String name;

    private String surname;

    private String fullName;

    private ParticipantStatus status;

    private InvitationStatus invitationStatus;

    private String email;

    // TODO(mr): 15.11.2018 age
}
