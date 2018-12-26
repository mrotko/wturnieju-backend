package pl.wturnieju.service;

import lombok.Data;

@Data
public class ResetPasswordVerificationData extends VerificationData {

    private String email;
}
