package pl.wturnieju.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class TournamentParticipationRequestVerificationToken extends VerificationToken {

    private String tournamentId;
}
