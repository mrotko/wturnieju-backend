package pl.wturnieju.model;

import pl.wturnieju.model.generic.GenericProfile;

public class StaffProfile extends GenericProfile {

    public StaffProfile() {
        init();
    }

    private void init() {
        profileType = ProfileType.STAFF;
    }
}
