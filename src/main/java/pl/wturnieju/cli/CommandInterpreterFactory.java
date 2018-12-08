package pl.wturnieju.cli;

import pl.wturnieju.model.User;
import pl.wturnieju.search.ISearch;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;

public class CommandInterpreterFactory {
    public static CommandInterpreter createInterpreter(
            IUserService userService,
            ITournamentService tournamentService,
            ISearch<String, User> userSearch,
            ICommandParsedDataProvider parsedDataProvider) {
        var commandName = parsedDataProvider.getCommandName();
        switch (commandName) {
        case "settings":
            return new SettingsCommandInterpreter(userService, parsedDataProvider);
        case "user":
            return new UserCommandInterpreter(userService, userSearch, tournamentService, parsedDataProvider);
        case "tournament":
            return new TournamentCommandInterpreter(tournamentService, parsedDataProvider);
        default:
            throw new IllegalArgumentException("Unknown command name: " + commandName);
        }
    }
}
