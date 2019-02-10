package pl.wturnieju.cli;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.cli.dto.CliResponse;
import pl.wturnieju.cli.dto.CommandDTO;
import pl.wturnieju.helper.ITournamentHelper;
import pl.wturnieju.model.User;
import pl.wturnieju.model.verification.ChangeEmailVerificationToken;
import pl.wturnieju.search.ISearch;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;

@RestController
@RequestMapping("/cli")
@RequiredArgsConstructor
public class CliController {

    private final IUserService userService;

    private final ITournamentService tournamentService;

    @Qualifier("userSimpleSearchService")
    private final ISearch<String, User> userSearch;

    @Qualifier("emailChangeTokenVerificationService")
    private final IVerificationService<ChangeEmailVerificationToken> verificationService;

    private final ITournamentHelper tournamentHelper;

    @PostMapping
    public CliResponse performCliCommand(@RequestBody @NonNull CommandDTO dto) {
        var parser = new CliCommandParser(dto.getCommand());
        try {
            parser.parse();
            var commandInterpreter = CommandInterpreterFactory.createInterpreter(
                    userService, tournamentService, tournamentHelper, userSearch, verificationService, parser);
            return commandInterpreter.getResponse();
        } catch (Exception e) {
            var response = new CliResponse();
            if (response.getErrorMessages() == null) {
                response.setErrorMessages(new ArrayList<>());
            }
            response.getErrorMessages().add(e.getMessage());
            return response;
        }

    }
}
