package pl.wturnieju.cli;

import pl.wturnieju.cli.dto.SettingsInfoResponse;
import pl.wturnieju.model.ChangeEmailVerificationToken;
import pl.wturnieju.service.EmailChangeVerificationData;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;

public class SettingsCommandInterpreter extends CommandInterpreter<SettingsInfoResponse> {

    private final IUserService userService;

    private final IVerificationService<ChangeEmailVerificationToken> verificationService;

    public SettingsCommandInterpreter(IUserService userService,
            IVerificationService<ChangeEmailVerificationToken> verificationService,
            ICommandParsedDataProvider parsedDataProvider) {
        super(parsedDataProvider);
        this.userService = userService;
        this.verificationService = verificationService;
    }

    @Override
    SettingsInfoResponse getResponse() {
        var dto = new SettingsInfoResponse();

        passwordParameterTask(dto);
        emailParameterTask(dto);
        nameParameterTask(dto);
        surnameParameterTask(dto);

        emailFlagTask(dto);
        nameFlagTask(dto);
        surnameFlagTask(dto);
        fullNameFlagTask(dto);

        return dto;
    }

    private void emailFlagTask(SettingsInfoResponse dto) {
        doCommandTask(dto, () -> {
            if (isFlagExists("email", "e")) {
                dto.setEmail(userService.getCurrentUser().getUsername());
            }
        });
    }

    private void fullNameFlagTask(SettingsInfoResponse dto) {
        doCommandTask(dto, () -> {
            if (isFlagExists("fullName", "fn")) {
                dto.setFullName(userService.getCurrentUser().getFullName());
            }
        });
    }

    private void nameFlagTask(SettingsInfoResponse dto) {
        doCommandTask(dto, () -> {
            if (isFlagExists("name", "n")) {
                dto.setName(userService.getCurrentUser().getName());
            }
        });
    }

    private void surnameFlagTask(SettingsInfoResponse dto) {
        doCommandTask(dto, () -> {
            if (isFlagExists("surname", "s")) {
                dto.setSurname(userService.getCurrentUser().getSurname());
            }
        });
    }

    private void surnameParameterTask(SettingsInfoResponse dto) {
        doCommandTask(dto, () -> {
            getParameterValue("surname", "s").ifPresent(surname -> {
                userService.setSurname(surname);
                dto.setSurname(surname);
            });
        });
    }

    private void nameParameterTask(SettingsInfoResponse dto) {
        doCommandTask(dto, () -> {
            getParameterValue("name", "n").ifPresent(name -> {
                userService.setName(name);
                dto.setName(name);
            });
        });
    }

    private void emailParameterTask(SettingsInfoResponse dto) {
        doCommandTask(dto, () -> {
            var password = getParameterValue("password", "p");
            var email = getParameterValue("email", "e");

            if (password.isPresent() && email.isPresent()) {
                userService.validateEmailChange(email.get(), password.get());

                var data = new EmailChangeVerificationData();
                data.setNewEmail(email.get());
                data.setOldEmail(userService.getCurrentUser().getUsername());

                verificationService.createVerificationToken(data);
                dto.setEmail(email.get());
            }
        });
    }

    private void passwordParameterTask(SettingsInfoResponse dto) {
        doCommandTask(dto, () -> {
            var password = getParameterValue("password", "p");
            var oldPassword = getParameterValue("oldPassword", "o");
            var email = getParameterValue("email", "e");
            if (password.isPresent() && oldPassword.isPresent() && !email.isPresent()) {
                userService.changePassword(password.get(), oldPassword.get());
                dto.setPasswordChanged(true);
            }
        });
    }
}
