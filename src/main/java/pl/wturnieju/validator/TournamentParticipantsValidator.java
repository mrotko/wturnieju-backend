package pl.wturnieju.validator;

import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.Tournament;

public class TournamentParticipantsValidator implements IValidator<Tournament> {
    @Override
    public boolean validate(Tournament tested) {
        try {
            validateAndThrowInvalid(tested);
        } catch (ValidationException e) {
            return false;
        }
        return true;
    }

    @Override
    public void validateAndThrowInvalid(Tournament tested) throws ValidationException {
        var acceptedParticipantsNumber = tested.getParticipants().stream()
                .filter(p -> p.getParticipantStatus() == ParticipantStatus.INVITED)
                .filter(p -> p.getInvitationStatus() == InvitationStatus.ACCEPTED)
                .count();
        if (acceptedParticipantsNumber < tested.getMinParticipants()) {
            throw new ValidationException("Participant validation error. Required min " + tested
                    .getMinParticipants() + " but received " + acceptedParticipantsNumber);
        }
        if (acceptedParticipantsNumber > tested.getMaxParticipants()) {
            throw new ValidationException("Participant validation error. Required max " + tested
                    .getMaxParticipants() + " but received " + acceptedParticipantsNumber);
        }
    }
}
