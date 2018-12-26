package pl.wturnieju.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ChangeEmailVerificationToken extends VerificationToken {

    private String oldEmail;

    private String newEmail;
}
