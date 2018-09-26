package pl.wturnieju.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.ProfileType;
import pl.wturnieju.service.IUserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements IUserController {

    private final IUserService userService;

    @Override
    @PostMapping(value = "/profile", params = {"profileType", "competitionType"})
    public void createProfile(
            @RequestParam("profileType") ProfileType profileType,
            @RequestParam("competitionType") CompetitionType competitionType) {

    }

    @Override
    @PostMapping("/password/{password}")
    public void changePassword(@PathVariable("password") String password) {
        try {
            userService.changePassword(password);
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    @PostMapping("/email/{email}")
    public void changeEmail(@PathVariable("email") String email) {
        userService.changeEmail(email);
    }
}
