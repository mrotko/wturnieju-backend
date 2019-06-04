package pl.wturnieju.aspect;

import static java.util.Optional.ofNullable;
import static pl.wturnieju.utils.JoinPointUtils.getAnnotation;
import static pl.wturnieju.utils.JoinPointUtils.getParameterValue;

import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.access.tournament.validator.AccessLevelValidatorConfiguration;
import pl.wturnieju.access.tournament.validator.IAccessLevelValidator;
import pl.wturnieju.annotation.CheckTournamentAccess;
import pl.wturnieju.annotation.TournamentId;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.tournament.Tournament;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckTournamentAccessAspect {

    private final ITournamentService tournamentService;

    private final AccessLevelValidatorConfiguration configuration;

    @Pointcut(value = "@annotation(pl.wturnieju.annotation.CheckTournamentAccess)")
    private void checkTournamentAccess() {
    }

    @Around("checkTournamentAccess()")
    public Object checkIfUserHasAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        findTournament(joinPoint).ifPresent(tournament -> getAccessLevelValidator(joinPoint).checkAccess(tournament));
        return joinPoint.proceed();
    }

    private IAccessLevelValidator getAccessLevelValidator(JoinPoint joinPoint) {
        var checkTournamentAccess = getAnnotation(joinPoint, CheckTournamentAccess.class);
        return configuration.getAccessLevelValidator(checkTournamentAccess);
    }

    protected Optional<Tournament> findTournament(JoinPoint joinPoint) {
        return findTournamentId(joinPoint).map(tournamentService::getById);
    }

    protected Optional<String> findTournamentId(JoinPoint joinPoint) {
        return ofNullable(getParameterValue(joinPoint, TournamentId.class)).map(Object::toString);
    }
}
