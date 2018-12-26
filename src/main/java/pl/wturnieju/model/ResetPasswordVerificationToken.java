package pl.wturnieju.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ResetPasswordVerificationToken extends VerificationToken {

    private String email;
}
