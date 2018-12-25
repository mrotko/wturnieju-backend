package pl.wturnieju.dto.user;

import lombok.Data;

@Data
public class ChangePasswordDTO {

    private String newPassword;

    private String oldPassword;
}
