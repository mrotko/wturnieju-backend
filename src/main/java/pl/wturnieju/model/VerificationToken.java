package pl.wturnieju.model;

import lombok.Data;

@Data
public abstract class VerificationToken extends Persistent {

    private String token;

    private Timestamp expiryDate;
}
