package pl.wturnieju.dto.user;

import lombok.Data;

@Data
public class ChangeUsernameDTO {
    private String username;
    private String password;
}
