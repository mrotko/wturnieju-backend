package pl.wturnieju.cli;

import pl.wturnieju.helper.ITournamentHelper;
import pl.wturnieju.model.User;
import pl.wturnieju.search.ISearch;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;

public class CommandInterpreterFactory {
    public static CommandInterpreter createInterpreter(
            IUserService userService,
            ITournamentService tournamentService,
            ITournamentHelper tournamentHelper,
            ISearch<String, User> userSearch,
            IVerificationService verificationService,
            ICommandParsedDataProvider parsedDataProvider) {
        var commandName = parsedDataProvider.getCommandName();
        switch (commandName) {
        case "settings":
            return new SettingsCommandInterpreter(userService, verificationService, parsedDataProvider);
        case "user":
            return new UserCommandInterpreter(userService, userSearch, tournamentHelper, parsedDataProvider);
        case "tournament":
            return new TournamentCommandInterpreter(tournamentService, parsedDataProvider);
        default:
            throw new IllegalArgumentException("Unknown command name: " + commandName);
        }
    }
}
