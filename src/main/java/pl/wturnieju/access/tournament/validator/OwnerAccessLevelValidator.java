package pl.wturnieju.access.tournament.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import pl.wturnieju.exception.TournamentAccessDeniedException;
import pl.wturnieju.model.User;
import pl.wturnieju.service.ICurrentUserProvider;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Tournament;

@Component
public class OwnerAccessLevelValidator extends AbstractAccessLevelValidator {
    public OwnerAccessLevelValidator(ICurrentUserProvider currentUserProvider,
            IParticipantService participantService) {
        super(currentUserProvider, participantService);
    }

    @Override
    public void checkAccess(Tournament tournament) {
        var user = currentUserProvider.getCurrentUser();
        denyIfNotLoggedIn(user);
        denyIfUserIsNotTournamentOwner(tournament, user);
    }

    private void denyIfUserIsNotTournamentOwner(Tournament tournament, User user) {
        if (!Objects.equals(tournament.getOwner().getId(), user.getId())) {
            throw new TournamentAccessDeniedException();
        }
    }
}
