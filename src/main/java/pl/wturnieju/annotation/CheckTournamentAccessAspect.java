package pl.wturnieju.annotation;

import java.lang.reflect.Parameter;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.wturnieju.exception.TournamentAccessDeniedException;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.User;
import pl.wturnieju.service.ICurrentUserProvider;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.tournament.Member;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.Tournament;

@Aspect
@Component
public class CheckTournamentAccessAspect {

    @Autowired
    private ITournamentService tournamentService;

    @Autowired
    private ICurrentUserProvider currentUserProvider;

    @Autowired
    private IParticipantService participantService;

    @Pointcut(value = "@annotation(pl.wturnieju.annotation.CheckTournamentAccess)")
    private void checkTournamentAccess() {

    }

    @Around("checkTournamentAccess()")
    public Object checkIfUserHasAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        var checkTournamentAccess = getMethodSignature(joinPoint).getMethod()
                .getAnnotation(CheckTournamentAccess.class);
        var accessLevel = checkTournamentAccess.accessLevel();
        String tournamentId = getTournamentId(joinPoint);
        if (tournamentId != null) {
            var tournament = tournamentService.getById(tournamentId);
            if (accessLevel == TournamentAccessLevel.USER) {
                checkUserLevelAccess(tournament);
            } else if (accessLevel == TournamentAccessLevel.OWNER) {
                checkOwnerLevelAccess(tournament);
            }
        }

        return joinPoint.proceed();
    }

    protected void checkUserLevelAccess(Tournament tournament) {
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

    private void denyIfNotLoggedIn(User user) {
        if (user == null) {
            throw new TournamentAccessDeniedException();
        }
    }

    protected void checkOwnerLevelAccess(Tournament tournament) {
        var user = currentUserProvider.getCurrentUser();
        denyIfNotLoggedIn(user);
        denyIfUserIsNotTournamentOwner(tournament, user);
    }

    private void denyIfUserIsNotTournamentOwner(Tournament tournament, User user) {
        if (!Objects.equals(tournament.getOwner().getId(), user.getId())) {
            throw new TournamentAccessDeniedException();
        }
    }

    protected MethodSignature getMethodSignature(ProceedingJoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }

    protected String getTournamentId(ProceedingJoinPoint joinPoint) {
        var methodSignature = getMethodSignature(joinPoint);
        var parameterId = getTournamentIdParameterIndex(methodSignature.getMethod().getParameters());
        if (parameterId != null) {
            return (String) joinPoint.getArgs()[parameterId];
        }
        return null;
    }

    protected Integer getTournamentIdParameterIndex(Parameter[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getAnnotation(TournamentId.class) != null) {
                return i;
            }
        }
        return null;
    }
}
