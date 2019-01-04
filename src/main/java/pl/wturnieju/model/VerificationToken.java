package pl.wturnieju.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class VerificationToken extends Persistent {

    private String token;

    private Timestamp expiryDate;
}
