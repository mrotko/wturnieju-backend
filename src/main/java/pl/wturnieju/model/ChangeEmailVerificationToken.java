package pl.wturnieju.model;

import lombok.Data;

@Data
public class ChangeEmailVerificationToken extends VerificationToken {

    private String oldEmail;

    private String newEmail;
}
