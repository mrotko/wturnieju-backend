package pl.wturnieju.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.ChangeEmailVerificationToken;
import pl.wturnieju.model.NewAccountVerificationToken;
import pl.wturnieju.model.ResetPasswordVerificationToken;
import pl.wturnieju.model.TournamentInviteVerificationToken;
import pl.wturnieju.model.TournamentParticipationRequestVerificationToken;
import pl.wturnieju.service.ITournamentParticipantService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;

@RequestMapping("/verification")
@RestController
@RequiredArgsConstructor
public class VerificationController {

    private static final String ACCOUNT_SERVICE_TYPE = "account";

    private static final String PASSWORD_SERVICE_TYPE = "password";

    private static final String TOURNAMENT_INVITATION_SERVICE_TYPE = "tournament-invitation";

    private static final String EMAIL_SERVICE_TYPE = "email";

    private static final String TOURNAMENT_PARTICIPATION_REQUEST_SERVICE_TYPE = "tournament-participation-request";

    private final IUserService userService;

    private final ITournamentParticipantService tournamentParticipantService;

    @Qualifier("newAccountTokenVerificationService")
    private final IVerificationService<NewAccountVerificationToken> newAccountVerificationService;

    @Qualifier("emailChangeTokenVerificationService")
    private final IVerificationService<ChangeEmailVerificationToken> emailChangeVerificationService;

    @Qualifier("resetPasswordTokenVerificationService")
    private final IVerificationService<ResetPasswordVerificationToken> resetPasswordVerificationService;

    @Qualifier("tournamentInviteTokenVerificationService")
    private final IVerificationService<TournamentInviteVerificationToken> tournamentInviteVerificationService;

    @Qualifier("tournamentParticipationRequestVerificationService")
    private final IVerificationService<TournamentParticipationRequestVerificationToken> tournamentParticipationRequestVerificationService;

    @PatchMapping(path = "/" + EMAIL_SERVICE_TYPE, params = "token")
    public void verifyChangedEmail(@RequestParam("token") String token) {
        var verifiedToken = emailChangeVerificationService.getValidToken(token);
        if (verifiedToken == null) {
            throw new ResourceNotFoundException("Token not found");
        }
        userService.confirmChangedEmail(verifiedToken.getOldEmail(), verifiedToken.getNewEmail());
        emailChangeVerificationService.deleteToken(token);
    }

    @PatchMapping(path = "/" + ACCOUNT_SERVICE_TYPE, params = "token")
    public void verifyNewAccount(@RequestParam("token") String token) {
        var verifiedToken = newAccountVerificationService.getValidToken(token);
        if (verifiedToken == null) {
            throw new ResourceNotFoundException("Token not found");
        }
        userService.confirmNewAccount(verifiedToken.getEmail());
        newAccountVerificationService.deleteToken(token);
    }

    @PatchMapping(path = "/" + PASSWORD_SERVICE_TYPE, params = "token")
    public void changePassword(@RequestParam("token") String token, @RequestBody String password) {
        var verifiedToken = resetPasswordVerificationService.getValidToken(token);
        if (verifiedToken == null) {
            throw new ResourceNotFoundException("Token not found");
        }
        userService.resetPassword(verifiedToken.getEmail(), password);
        resetPasswordVerificationService.deleteToken(token);
    }

    @PatchMapping(path = "/" + TOURNAMENT_INVITATION_SERVICE_TYPE, params = "token")
    public Boolean reactToTournamentInvitation(@RequestParam("token") String token,
            @RequestBody @NonNull Boolean decision) {
        var verifiedToken = tournamentInviteVerificationService.getValidToken(token);
        if (verifiedToken == null) {
            throw new ResourceNotFoundException("Token not found");
        }

        if (decision) {
            tournamentParticipantService.acceptInvitation(verifiedToken.getTournamentId(),
                    verifiedToken.getParticipantId());
        } else {
            tournamentParticipantService.rejectInvitation(verifiedToken.getTournamentId(),
                    verifiedToken.getParticipantId());
        }
        tournamentInviteVerificationService.deleteToken(token);
        return decision;
    }

    @PatchMapping(path = "/" + TOURNAMENT_PARTICIPATION_REQUEST_SERVICE_TYPE, params = "token")
    public void addUserToTournamentParticipants(@RequestParam("token") String token,
            @RequestBody @NonNull String userId) {
        var verifiedToken = tournamentParticipationRequestVerificationService.getValidToken(token);
        if (verifiedToken == null) {
            throw new ResourceNotFoundException("Token not found");
        }
        tournamentParticipantService.requestParticipation(verifiedToken.getTournamentId(), userId);
    }

    @GetMapping(path = "/checkToken", params = {"token", "service"})
    public Boolean checkToken(@RequestParam("token") String token, @RequestParam("service") String serviceName) {
        var service = getVerificationService(serviceName);
        return service.getValidToken(token) != null;
    }

    private IVerificationService getVerificationService(String serviceName) {
        switch (serviceName) {
        case ACCOUNT_SERVICE_TYPE:
            return newAccountVerificationService;
        case EMAIL_SERVICE_TYPE:
            return emailChangeVerificationService;
        case PASSWORD_SERVICE_TYPE:
            return resetPasswordVerificationService;
        case TOURNAMENT_INVITATION_SERVICE_TYPE:
            return tournamentInviteVerificationService;
        case TOURNAMENT_PARTICIPATION_REQUEST_SERVICE_TYPE:
            return tournamentParticipationRequestVerificationService;
        default:
            throw new IllegalArgumentException("Unknown enum type " + serviceName);
        }
    }
}
