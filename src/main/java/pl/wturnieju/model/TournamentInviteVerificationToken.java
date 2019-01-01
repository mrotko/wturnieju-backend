package pl.wturnieju.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class TournamentInviteVerificationToken extends VerificationToken {

    private String tournamentId;

    private String participantId;
}
