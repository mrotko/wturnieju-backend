package pl.wturnieju.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewAccountVerificationData extends VerificationData {

    private String email;
}
