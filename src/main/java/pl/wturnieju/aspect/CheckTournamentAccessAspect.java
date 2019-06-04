package pl.wturnieju.aspect;

import static java.util.Optional.ofNullable;
import static pl.wturnieju.utils.JoinPointUtils.getAnnotation;
import static pl.wturnieju.utils.JoinPointUtils.getParameterValue;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.access.tournament.validator.AccessLevelValidatorConfiguration;
import pl.wturnieju.annotation.CheckTournamentAccess;
import pl.wturnieju.annotation.TournamentId;
import pl.wturnieju.service.ITournamentService;

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
        ofNullable(getTournamentId(joinPoint)).ifPresent(tournamentId -> {
            var checkTournamentAccess = getAnnotation(joinPoint, CheckTournamentAccess.class);
            var accessLevelValidator = configuration.getAccessLevelValidator(checkTournamentAccess);
            accessLevelValidator.checkAccess(tournamentService.getById(tournamentId));
        });

        return joinPoint.proceed();
    }

    protected String getTournamentId(ProceedingJoinPoint joinPoint) {
        return (String) getParameterValue(joinPoint, TournamentId.class);
    }
}
