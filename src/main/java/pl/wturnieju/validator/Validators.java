package pl.wturnieju.validator;

import pl.wturnieju.tournament.Tournament;

public class Validators {

    public static IValidator<String> getEmailValidator() {
        return new EmailValidator();
    }

    public static IValidator<String> getPasswordValidator() {
        return new PasswordValidator();
    }

    public static IValidator<Tournament> getTournamentParticipantsValidator() {
        return new TournamentParticipantsValidator();
    }
}
