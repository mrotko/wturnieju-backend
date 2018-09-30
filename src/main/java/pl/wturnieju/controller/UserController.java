package pl.wturnieju.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.user.ChangePasswordDTO;
import pl.wturnieju.dto.user.ChangeUsernameDTO;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.service.IUserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PutMapping("/password")
    public void changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            userService.changePassword(changePasswordDTO.getPassword());
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @PutMapping("/email")
    public void changeEmail(@RequestBody ChangeUsernameDTO changeUsernameDTO) {
        try {
            userService.changeEmail(changeUsernameDTO.getUsername(), changeUsernameDTO.getPassword());
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
