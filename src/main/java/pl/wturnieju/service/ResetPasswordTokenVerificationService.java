package pl.wturnieju.service;

import org.springframework.stereotype.Service;

import pl.wturnieju.model.ResetPasswordVerificationToken;
import pl.wturnieju.repository.TokenVerificationRepository;

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
