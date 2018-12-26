package pl.wturnieju.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
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
import pl.wturnieju.model.ChangeEmailVerificationToken;
import pl.wturnieju.model.UserGrantedAuthority;
import pl.wturnieju.service.EmailChangeVerificationData;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;

@RestController
@RequestMapping("/userSettings")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @Qualifier("emailChangeTokenVerificationService")
    private final IVerificationService<ChangeEmailVerificationToken> verificationService;

    @PutMapping("/password")
    public void changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO.getNewPassword(), changePasswordDTO.getOldPassword());
    }

    @PutMapping("/email")
    public void changeEmail(@RequestBody ChangeUsernameDTO changeUsernameDTO) {
        userService.validateEmailChange(changeUsernameDTO.getUsername(), changeUsernameDTO.getPassword());

        var data = new EmailChangeVerificationData();
        data.setNewEmail(changeUsernameDTO.getUsername());
        data.setOldEmail(userService.getCurrentUser().getUsername());

        verificationService.createVerification(data);
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
