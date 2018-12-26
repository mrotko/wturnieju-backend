package pl.wturnieju.model;

import lombok.Data;

@Data
public class NewAccountVerificationToken extends VerificationToken {

    private String email;
}
