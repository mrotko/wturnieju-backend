package pl.wturnieju.service.impl.verification;

import org.springframework.stereotype.Service;

import pl.wturnieju.model.verification.TournamentInviteVerificationData;
import pl.wturnieju.model.verification.TournamentInviteVerificationToken;
import pl.wturnieju.model.verification.VerificationData;
import pl.wturnieju.repository.TokenVerificationRepository;
import pl.wturnieju.service.IEmailService;

@Service
public class TournamentInviteTokenVerificationService extends TokenVerificationService<TournamentInviteVerificationToken> {
    public TournamentInviteTokenVerificationService(TokenVerificationRepository tokenVerificationRepository,
            IEmailService emailService) {
        super(tokenVerificationRepository, emailService);
    }

    @Override
    public TournamentInviteVerificationToken createVerificationToken(VerificationData verificationData) {
        var data = (TournamentInviteVerificationData) verificationData;

        TournamentInviteVerificationToken token = new TournamentInviteVerificationToken();

        token.setTournamentId(data.getTournamentId());
        token.setParticipantId(data.getUserId());

        setExpiryDateAndGenerateToken(token);

        emailService.sendSimpleMessage(
                data.getEmail(),
                "You have been invted to tournament.",
                "Click on this link: " + applicationUrl + "/verification/tournament-invitation" +
                        "?token=" + token.getToken()
                        + "&tournamentId=" + token.getTournamentId()
        );

        return tokenVerificationRepository.save(token);
    }
}
