package pl.wturnieju.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmailChangeVerificationData extends VerificationData {

    private String newEmail;

    private String oldEmail;
}
