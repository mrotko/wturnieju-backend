package pl.wturnieju.service;

import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.VerificationToken;
import pl.wturnieju.repository.TokenVerificationRepository;

@RequiredArgsConstructor
public abstract class TokenVerificationService<T extends VerificationToken> implements IVerificationService<T> {

    protected final TokenVerificationRepository tokenVerificationRepository;

    protected final IEmailService emailService;

    @Value("${path.application-url}")
    protected String applicationUrl;

    @Override
    public T getValidToken(String token) {
        var verificationToken = tokenVerificationRepository.findByToken(token).orElse(null);
        if (verificationToken == null) {
            return null;
        }
        if (!validateExpiryDate(verificationToken)) {
            return null;
        }
        return (T) verificationToken;
    }

    @Override
    public void deleteToken(String token) {
        tokenVerificationRepository.deleteByToken(token);
    }

    protected boolean validateExpiryDate(VerificationToken token) {
        if (token.getExpiryDate() == null) {
            return false;
        }
        if (token.getExpiryDate().getValue().isBefore(Timestamp.now().getValue())) {
            return false;
        }
        return true;
    }

    protected Timestamp getDefaultTokenExpiryDate() {
        return new Timestamp(Timestamp.now().getValue().plusDays(1));
    }
}
