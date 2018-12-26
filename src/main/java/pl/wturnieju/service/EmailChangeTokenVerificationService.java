package pl.wturnieju.service;

import java.time.LocalDateTime;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import pl.wturnieju.model.ChangeEmailVerificationToken;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.repository.TokenVerificationRepository;

@Service
public class EmailChangeTokenVerificationService extends TokenVerificationService<ChangeEmailVerificationToken> {

    public EmailChangeTokenVerificationService(TokenVerificationRepository tokenVerificationRepository,
            IEmailService emailService) {
        super(tokenVerificationRepository, emailService);
    }

    @Override
    public void createVerification(VerificationData verificationData) {
        var data = (EmailChangeVerificationData) verificationData;
        var token = new ChangeEmailVerificationToken();

        token.setOldEmail(data.getOldEmail());
        token.setNewEmail(data.getNewEmail());
        token.setExpiryDate(new Timestamp(LocalDateTime.now().plusDays(1)));
        token.setToken(DigestUtils.sha512Hex(token.toString()));

        emailService.sendSimpleMessage(
                data.getNewEmail(),
                "Confirm email change",
                "Click on this link: " + applicationUrl + "/verification/email?token=" + token.getToken()
        );

        tokenVerificationRepository.save(token);
    }
}
