package pl.wturnieju.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResetPasswordVerificationData extends VerificationData {

    private String email;
}
