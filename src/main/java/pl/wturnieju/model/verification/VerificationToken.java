package pl.wturnieju.model.verification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class VerificationToken extends Persistent {

    private String token;

    private Timestamp expiryDate;
}
