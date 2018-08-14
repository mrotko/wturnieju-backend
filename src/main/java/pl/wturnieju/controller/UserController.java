package pl.wturnieju.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.wturnieju.dto.ForgetPasswordDTO;
import pl.wturnieju.dto.RegistrationDTO;
import pl.wturnieju.dto.ResetPasswordDTO;
import pl.wturnieju.model.User;
import pl.wturnieju.service.IUserService;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private IUserService userService;

    @PostMapping("/register")
    public User register(@RequestBody RegistrationDTO registrationDTO) {
        //        TODO exception error handling
        return userService.create(User.builder()
                .username(registrationDTO.getEmail())
                .password(registrationDTO.getPassword())
                .build());
    }

    @PostMapping("/forget-password")
    public void forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        //        TODO exception error handling

    }

    @PostMapping("reset-password")
    public void resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        //        TODO exception error handling

    }
}
