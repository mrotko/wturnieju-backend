package pl.wturnieju.validator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import pl.wturnieju.exception.ValidationException;

public class EmailValidator implements IValidator<String> {

    @Override
    public boolean validate(String email) {
        try {
            validateAndThrowInvalid(email);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void validateAndThrowInvalid(String email) throws ValidationException {
        try {
            InternetAddress address = new InternetAddress(email);
            address.validate();
        } catch (AddressException e) {
            throw new ValidationException("Invalid email format");
        }
    }
}
