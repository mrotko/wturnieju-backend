package pl.wturnieju.access.tournament.validator;

import pl.wturnieju.tournament.Tournament;

public interface IAccessLevelValidator {

    void checkAccess(Tournament tournament);
}
