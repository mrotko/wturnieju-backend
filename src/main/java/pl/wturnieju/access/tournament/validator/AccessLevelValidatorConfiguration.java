package pl.wturnieju.access.tournament.validator;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.annotation.CheckTournamentAccess;

@Configuration
@RequiredArgsConstructor
public class AccessLevelValidatorConfiguration {

    private final OwnerAccessLevelValidator ownerAccessLevelValidator;

    private final UserAccessLevelValidator userAccessLevelValidator;

    @NonNull
    public IAccessLevelValidator getAccessLevelValidator(@NonNull CheckTournamentAccess accessLevel) {
        return switch (accessLevel.accessLevel()) {
            case OWNER -> ownerAccessLevelValidator;
            case USER -> userAccessLevelValidator;
        };
    }
}
