package pl.wturnieju.controller;

import org.springframework.web.bind.annotation.RequestBody;

import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.ProfileType;

public interface IUserController {

    void createProfile(ProfileType profileType, CompetitionType competitionType);

    void changePassword(@RequestBody String password);

    void changeEmail(String email);


}
