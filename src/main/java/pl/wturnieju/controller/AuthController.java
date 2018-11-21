package pl.wturnieju.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.auth.ForgetPasswordDTO;
import pl.wturnieju.dto.auth.RegistrationDTO;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.exception.ValueExistsException;
import pl.wturnieju.inserter.UserInserter;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.IEmailService;
import pl.wturnieju.service.IUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    // TODO(mr): 21.11.2018 to remove
    @Autowired
    private UserRepository userRepository;

    private final IUserService userService;

    private final IEmailService emailService;

    @PostMapping("/inserter")
    public void insertUsers() {
        UserInserter inserter = new UserInserter(userService, userRepository);
        inserter.insertUsersToDatabase();
    }

    @PostMapping("/register")
    public void register(@RequestBody RegistrationDTO registrationDTO) {
        try {
            userService.create(registrationDTO.getUsername(), registrationDTO.getPassword());
        } catch (IllegalArgumentException e) {
            throw new ValueExistsException(e.getMessage());
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @PostMapping("/forget-password")
    public void forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        var user = userService.loadUserByUsername(forgetPasswordDTO.getUsername());
        var newPass = RandomStringUtils.random(20, true, true);
        try {
            userService.changePassword(user.getUsername(), newPass);
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e);
        }
        emailService.sendSimpleMessage(user.getUsername(), "Zmiana hasła", "nowe hasło " + newPass);
    }
}
