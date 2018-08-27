package pl.wturnieju.model;

import pl.wturnieju.model.generic.GenericProfile;

public class PlayerProfile extends GenericProfile {

    public PlayerProfile() {
        init();
    }

    private void init() {
        profileType = ProfileType.PLAYER;
    }
}
