package pl.wturnieju.generator;

import pl.wturnieju.model.User;

public class CurrentUserGenerator {

    public static User generateUser() {
        return User.builder()
                .name("Adam")
                .surname("Smith")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .enabled(true)
                .password("CNw3FZ82s-&d9uyx")
                .build();
    }


}
