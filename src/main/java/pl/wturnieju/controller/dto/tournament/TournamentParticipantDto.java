package pl.wturnieju.controller.dto.tournament;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.tournament.ParticipantStatus;

@Getter
@Setter
public class TournamentParticipantDto {

    private String id;

    private String name;

    private String surname;

    private String fullName;

    private ParticipantStatus status;

    private InvitationStatus invitationStatus;

    private String email;

    // TODO(mr): 15.11.2018 age
}
