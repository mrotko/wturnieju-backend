package pl.wturnieju.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.config.AuthorityType;
import pl.wturnieju.dto.user.ChangePasswordDTO;
import pl.wturnieju.dto.user.ChangeUsernameDTO;
import pl.wturnieju.dto.user.UserConfigDTO;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.model.UserGrantedAuthority;
import pl.wturnieju.service.IUserService;

@RestController
@RequestMapping("/userSettings")
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

    @PutMapping("/authority")
    public Set<UserGrantedAuthority> setAuthorities(@RequestBody List<AuthorityType> authorityTypes) {
        userService.setAuthorities(authorityTypes.stream()
                .map(UserGrantedAuthority::new)
                .collect(Collectors.toList()));
        return userService.getCurrentUser().getAuthorities();
    }


    @GetMapping("config")
    public UserConfigDTO getUserConfig() {
        return new UserConfigDTO();
    }
}
