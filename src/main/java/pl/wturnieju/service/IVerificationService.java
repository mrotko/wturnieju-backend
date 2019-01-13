package pl.wturnieju.service;

import pl.wturnieju.model.verification.VerificationData;
import pl.wturnieju.model.verification.VerificationToken;

public interface IVerificationService<T extends VerificationToken> {

    T getValidToken(String token);

    void deleteToken(String token);

    T createVerificationToken(VerificationData verificationData);
}
