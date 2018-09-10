package pl.wturnieju.model;

import pl.wturnieju.model.generic.GenericProfile;

public class Contributor extends GenericProfile {

    public Contributor() {
        init();
    }

    private void init() {
        profileType = ProfileType.CONTRIBUTOR;
    }
}
