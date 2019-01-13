package pl.wturnieju.service.impl.verification;

import org.springframework.stereotype.Service;

import pl.wturnieju.model.verification.ResetPasswordVerificationData;
import pl.wturnieju.model.verification.ResetPasswordVerificationToken;
import pl.wturnieju.model.verification.VerificationData;
import pl.wturnieju.repository.TokenVerificationRepository;
import pl.wturnieju.service.IEmailService;

@Service
public class ResetPasswordTokenVerificationService extends TokenVerificationService<ResetPasswordVerificationToken> {

    public ResetPasswordTokenVerificationService(TokenVerificationRepository tokenVerificationRepository,
            IEmailService emailService) {
        super(tokenVerificationRepository, emailService);
    }

    @Override
    public ResetPasswordVerificationToken createVerificationToken(VerificationData verificationData) {
        var data = (ResetPasswordVerificationData) verificationData;

        var token = new ResetPasswordVerificationToken();

        token.setEmail(data.getEmail());
        setExpiryDateAndGenerateToken(token);

        emailService.sendSimpleMessage(
                data.getEmail(),
                "Reset your password",
                "Click on this link: " + applicationUrl + "/verification/password?token=" + token.getToken()
        );

        return tokenVerificationRepository.save(token);
    }
}
