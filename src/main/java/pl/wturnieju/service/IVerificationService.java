package pl.wturnieju.service;

import pl.wturnieju.model.VerificationToken;

public interface IVerificationService<T extends VerificationToken> {

    T getValidToken(String token);

    void deleteToken(String token);

    T createVerificationToken(VerificationData verificationData);
}
