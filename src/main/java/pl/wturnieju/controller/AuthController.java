package pl.wturnieju.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.config.user.AuthConfiguration;
import pl.wturnieju.controller.dto.auth.AuthConfigurationDto;
import pl.wturnieju.controller.dto.auth.AuthConfigurationMapper;
import pl.wturnieju.controller.dto.auth.ForgetPasswordDTO;
import pl.wturnieju.controller.dto.auth.RegistrationDTO;
import pl.wturnieju.model.verification.NewAccountVerificationData;
import pl.wturnieju.model.verification.NewAccountVerificationToken;
import pl.wturnieju.model.verification.ResetPasswordVerificationData;
import pl.wturnieju.model.verification.ResetPasswordVerificationToken;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final IUserService userService;

    private final AuthConfiguration authConfiguration;

    private final AuthConfigurationMapper authConfigurationMapper;

    @Qualifier("newAccountTokenVerificationService")
    private final IVerificationService<NewAccountVerificationToken> newAccountVerificationService;

    @Qualifier("resetPasswordTokenVerificationService")
    private final IVerificationService<ResetPasswordVerificationToken> resetPasswordVerificationService;

    @PostMapping("/register")
    public void register(@RequestBody RegistrationDTO registrationDTO) {
        userService.create(registrationDTO.getUsername(), registrationDTO.getPassword());

        var data = new NewAccountVerificationData();
        data.setEmail(registrationDTO.getUsername());
        newAccountVerificationService.createVerificationToken(data);
    }

    @GetMapping(value = "/active", params = "email")
    public boolean isAccountActive(@RequestParam("email") String email) {
        return userService.isAccountActive(email);
    }

    @PostMapping("/forget-password")
    public void forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        var data = new ResetPasswordVerificationData();
        data.setEmail(forgetPasswordDTO.getUsername());
        resetPasswordVerificationService.createVerificationToken(data);
    }

    @GetMapping("/config")
    public AuthConfigurationDto getConfiguration() {
        return authConfigurationMapper.mapToAuthConfigurationDto(authConfiguration.getAuthConfigurationData());
    }
}
