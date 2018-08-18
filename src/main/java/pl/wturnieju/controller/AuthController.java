package pl.wturnieju.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.ForgetPasswordDTO;
import pl.wturnieju.dto.RegistrationDTO;
import pl.wturnieju.dto.ResetPasswordDTO;
import pl.wturnieju.exception.ValueExistsException;
import pl.wturnieju.model.User;
import pl.wturnieju.service.IEmailService;
import pl.wturnieju.service.IUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final IUserService userService;

    private final IEmailService emailService;

    @PostMapping("/register")
    public void register(@RequestBody RegistrationDTO registrationDTO) throws ValueExistsException {
        try {
            userService.create(User.builder()
                    .username(registrationDTO.getEmail())
                    .password(registrationDTO.getPassword())
                    .build());
        } catch (IllegalArgumentException e) {
            throw new ValueExistsException("Given email exists. Email: " + registrationDTO.getEmail());
        }
    }

    @PostMapping("/forget-password")
    public void forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        var user = userService.loadUserByUsername(forgetPasswordDTO.getEmail());
        if (user.isAccountNonExpired()
                && user.isAccountNonLocked()
                && !user.isEnabled()) {
            var newPass = RandomStringUtils.random(20, true, true);
            userService.resetPassword(user.getUsername(), newPass);
            emailService.sendSimpleMessage(user.getUsername(), "Zmiana hasła", "nowe hasło" + newPass);
        }
    }

    @PostMapping("reset-password")
    public void resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {

    }
}
