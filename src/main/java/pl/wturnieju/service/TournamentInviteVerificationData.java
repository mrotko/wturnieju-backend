package pl.wturnieju.service;

import lombok.Data;

@Data
public class TournamentInviteVerificationData extends VerificationData {

    private String userId;

    private String email;

    private String tournamentId;
}
