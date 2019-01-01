package pl.wturnieju.service;

import org.springframework.stereotype.Service;

import pl.wturnieju.model.NewAccountVerificationToken;
import pl.wturnieju.repository.TokenVerificationRepository;

@Service
public class NewAccountTokenVerificationService extends TokenVerificationService<NewAccountVerificationToken> {

    public NewAccountTokenVerificationService(TokenVerificationRepository tokenVerificationRepository,
            IEmailService emailService) {
        super(tokenVerificationRepository, emailService);
    }

    @Override
    public NewAccountVerificationToken createVerificationToken(VerificationData verificationData) {
        var data = (NewAccountVerificationData) verificationData;
        var token = new NewAccountVerificationToken();

        token.setEmail(data.getEmail());
        setExpiryDateAndGenerateToken(token);

        emailService.sendSimpleMessage(
                data.getEmail(),
                "Confirm your account",
                "Click on this link: " + applicationUrl + "/verification/account?token=" + token.getToken()
        );

        return tokenVerificationRepository.save(token);
    }
}
