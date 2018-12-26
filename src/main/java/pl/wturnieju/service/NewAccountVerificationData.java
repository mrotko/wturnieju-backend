package pl.wturnieju.service;

import lombok.Data;

@Data
public class NewAccountVerificationData extends VerificationData {

    private String email;
}
