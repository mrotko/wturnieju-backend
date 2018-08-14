package pl.wturnieju.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginDTO {

    private String username;

    private String password;

    private boolean keepLogin;
}
