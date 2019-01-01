package pl.wturnieju.service;

import lombok.Data;

@Data
public class TournamentParticipationRequestVerificationData extends VerificationData {

    private String tournamentId;
}
