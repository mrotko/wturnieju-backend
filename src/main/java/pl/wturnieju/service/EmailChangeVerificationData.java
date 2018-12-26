package pl.wturnieju.service;

import lombok.Data;

@Data
public class EmailChangeVerificationData extends VerificationData {

    private String newEmail;

    private String oldEmail;
}
