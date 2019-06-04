package pl.wturnieju.access.tournament.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import pl.wturnieju.exception.TournamentAccessDeniedException;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.User;
import pl.wturnieju.service.ICurrentUserProvider;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Member;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.Tournament;

@Component
public class UserAccessLevelValidator extends AbstractAccessLevelValidator {

    public UserAccessLevelValidator(ICurrentUserProvider currentUserProvider,
            IParticipantService participantService) {
        super(currentUserProvider, participantService);
    }

    @Override
    public void checkAccess(Tournament tournament) {
        if (tournament.getAccessOption() == AccessOption.PRIVATE) {
            var user = currentUserProvider.getCurrentUser();
            denyIfNotLoggedIn(user);
            denyIfUserNotParticipateInTournament(tournament, user);
        }
    }

    private void denyIfUserNotParticipateInTournament(Tournament tournament, User user) {
        if (!Objects.equals(tournament.getOwner().getId(), user.getId())) {
            var participant = participantService.getTournamentParticipantByUserId(tournament.getId(),
                    user.getId());
            denyIfNoParticipant(participant);
            denyIfUserIsNotMemberOfParticipant(user, participant);
        }
    }

    private void denyIfNoParticipant(Participant participant) {
        if (participant == null) {
            throw new TournamentAccessDeniedException();
        }
    }

    private void denyIfUserIsNotMemberOfParticipant(User user, Participant participant) {
        var id = participant.getMembers().stream()
                .map(Member::getUserId)
                .filter(userId -> user.getId().equals(userId))
                .findFirst();

        if (id.isEmpty()) {
            throw new TournamentAccessDeniedException();
        }
    }
}
