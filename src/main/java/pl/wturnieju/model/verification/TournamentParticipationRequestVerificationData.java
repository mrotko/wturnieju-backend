package pl.wturnieju.model.verification;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TournamentParticipationRequestVerificationData extends VerificationData {

    private String tournamentId;
}
