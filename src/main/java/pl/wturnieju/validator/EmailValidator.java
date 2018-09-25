package pl.wturnieju.validator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailValidator implements IValidator<String> {

    @Override
    public boolean validate(String email) {
        try {
            InternetAddress address = new InternetAddress(email);
            address.validate();
        } catch (AddressException e) {
            return false;
        }
        return true;
    }
}
