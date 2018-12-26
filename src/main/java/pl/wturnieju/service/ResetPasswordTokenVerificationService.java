package pl.wturnieju.service;

import org.apache.commons.codec.digest.DigestUtils;
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
    public void createVerification(VerificationData verificationData) {
        var data = (ResetPasswordVerificationData) verificationData;

        var token = new ResetPasswordVerificationToken();

        token.setEmail(data.getEmail());
        token.setExpiryDate(getDefaultTokenExpiryDate());
        token.setToken(DigestUtils.sha512Hex(token.toString()));

        emailService.sendSimpleMessage(
                data.getEmail(),
                "Reset your password",
                "Click on this link: " + applicationUrl + "/verification/password?token=" + token.getToken()
        );

        tokenVerificationRepository.save(token);
    }
}
