package pl.wturnieju.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class NewAccountVerificationToken extends VerificationToken {

    private String email;
}
