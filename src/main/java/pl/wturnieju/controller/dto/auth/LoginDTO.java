package pl.wturnieju.controller.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    private String username;

    private String password;

    private boolean keepLogin;
}
