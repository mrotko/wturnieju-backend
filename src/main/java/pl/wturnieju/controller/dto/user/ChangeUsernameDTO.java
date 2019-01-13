package pl.wturnieju.controller.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUsernameDTO {

    private String username;

    private String password;
}
