package pl.wturnieju.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TournamentInviteVerificationToken extends VerificationToken {

    private String tournamentId;

    private String participantId;
}
