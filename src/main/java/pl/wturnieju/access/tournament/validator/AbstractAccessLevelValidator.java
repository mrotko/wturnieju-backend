package pl.wturnieju.access.tournament.validator;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.exception.TournamentAccessDeniedException;
import pl.wturnieju.model.User;
import pl.wturnieju.service.ICurrentUserProvider;
import pl.wturnieju.service.IParticipantService;

@RequiredArgsConstructor
public abstract class AbstractAccessLevelValidator implements IAccessLevelValidator {

    protected final ICurrentUserProvider currentUserProvider;

    protected final IParticipantService participantService;

    protected void denyIfNotLoggedIn(User user) {
        if (user == null) {
            throw new TournamentAccessDeniedException();
        }
    }
}
