package pl.wturnieju.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.auth.RegistrationDTO;
import pl.wturnieju.inserter.UserInserter;
import pl.wturnieju.model.NewAccountVerificationToken;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.IEmailService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;
import pl.wturnieju.service.NewAccountVerificationData;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    // TODO(mr): 21.11.2018 to remove
    @Autowired
    private UserRepository userRepository;

    private final IUserService userService;

    private final IEmailService emailService;


    @Qualifier("newAccountTokenVerificationService")
    private final IVerificationService<NewAccountVerificationToken> verificationService;

    @PostMapping("/inserter")
    public void insertUsers() {
        UserInserter inserter = new UserInserter(userService, userRepository);
        inserter.insertUsersToDatabase();
    }

    @PostMapping("/register")
    public void register(@RequestBody RegistrationDTO registrationDTO) {
        userService.create(registrationDTO.getUsername(), registrationDTO.getPassword());

        var data = new NewAccountVerificationData();
        data.setEmail(registrationDTO.getUsername());
        verificationService.createVerification(data);
    }

    @GetMapping(value = "/active", params = "email")
    public boolean isAccountActive(@RequestParam("email") String email) {
        return userService.isAccountActive(email);
    }

    // TODO(mr): 25.12.2018 impl nowej wersji
    //    @PostMapping("/forget-password")
    //    public void forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
    //        var user = userService.loadUserByUsername(forgetPasswordDTO.getUsername());
    //        var newPass = RandomStringUtils.random(20, true, true);
    //                userService.changePassword(user.getUsername(), newPass, );
    //        emailService.sendSimpleMessage(user.getUsername(), "Zmiana hasła", "nowe hasło " + newPass);
    //    }
}
