package pl.wturnieju.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.ChangeEmailVerificationToken;
import pl.wturnieju.model.NewAccountVerificationToken;
import pl.wturnieju.model.ResetPasswordVerificationToken;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;

@RequestMapping("/verification")
@RestController
@RequiredArgsConstructor
public class VerificationController {

    private final IUserService userService;

    @Qualifier("newAccountTokenVerificationService")
    private final IVerificationService<NewAccountVerificationToken> newAccountVerificationService;

    @Qualifier("emailChangeTokenVerificationService")
    private final IVerificationService<ChangeEmailVerificationToken> emailChangeVerificationService;

    @Qualifier("resetPasswordTokenVerificationService")
    private final IVerificationService<ResetPasswordVerificationToken> resetPasswordVerificationService;

    @PatchMapping(path = "/email", params = "token")
    public void verifyChangedEmail(@RequestParam("token") String token) {
        var verifiedToken = emailChangeVerificationService.getValidToken(token);
        if (verifiedToken == null) {
            throw new ResourceNotFoundException("Token not found");
        }
        userService.confirmChangedEmail(verifiedToken.getOldEmail(), verifiedToken.getNewEmail());
        emailChangeVerificationService.deleteToken(token);
    }

    @PatchMapping(path = "/account", params = "token")
    public void verifyNewAccount(@RequestParam("token") String token) {
        var verifiedToken = newAccountVerificationService.getValidToken(token);
        if (verifiedToken == null) {
            throw new ResourceNotFoundException("Token not found");
        }
        userService.confirmNewAccount(verifiedToken.getEmail());
        newAccountVerificationService.deleteToken(token);
    }

    @PatchMapping(path = "/password", params = "token")
    public void changePassword(@RequestParam("token") String token, @RequestBody String password) {
        var verifiedToken = resetPasswordVerificationService.getValidToken(token);
        if (verifiedToken == null) {
            throw new ResourceNotFoundException("Token not found");
        }
        userService.resetPassword(verifiedToken.getEmail(), password);
        resetPasswordVerificationService.deleteToken(token);
    }
}
