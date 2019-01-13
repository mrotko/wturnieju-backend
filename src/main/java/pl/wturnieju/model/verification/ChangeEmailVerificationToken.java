package pl.wturnieju.model.verification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChangeEmailVerificationToken extends VerificationToken {

    private String oldEmail;

    private String newEmail;
}
