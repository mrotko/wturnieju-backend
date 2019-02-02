package pl.wturnieju.model.verification;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TournamentInviteVerificationData extends VerificationData {

    private String participantId;

    private String email;

    private String tournamentId;
}
