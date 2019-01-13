package pl.wturnieju.service.impl.verification;

import org.springframework.stereotype.Service;

import pl.wturnieju.model.verification.ChangeEmailVerificationToken;
import pl.wturnieju.model.verification.EmailChangeVerificationData;
import pl.wturnieju.model.verification.VerificationData;
import pl.wturnieju.repository.TokenVerificationRepository;
import pl.wturnieju.service.IEmailService;

@Service
public class EmailChangeTokenVerificationService extends TokenVerificationService<ChangeEmailVerificationToken> {

    public EmailChangeTokenVerificationService(TokenVerificationRepository tokenVerificationRepository,
            IEmailService emailService) {
        super(tokenVerificationRepository, emailService);
    }

    @Override
    public ChangeEmailVerificationToken createVerificationToken(VerificationData verificationData) {
        var data = (EmailChangeVerificationData) verificationData;
        var token = new ChangeEmailVerificationToken();

        token.setOldEmail(data.getOldEmail());
        token.setNewEmail(data.getNewEmail());
        setExpiryDateAndGenerateToken(token);

        emailService.sendSimpleMessage(
                data.getNewEmail(),
                "Confirm email change",
                "Click on this link: " + applicationUrl + "/verification/email?token=" + token.getToken()
        );

        return tokenVerificationRepository.save(token);
    }
}
