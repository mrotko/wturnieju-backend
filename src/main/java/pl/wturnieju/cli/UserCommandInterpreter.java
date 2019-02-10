package pl.wturnieju.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.wturnieju.cli.dto.UserInfoResponse;
import pl.wturnieju.cli.dto.UserInfoResponseItem;
import pl.wturnieju.helper.ITournamentHelper;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.User;
import pl.wturnieju.search.ISearch;
import pl.wturnieju.service.IUserService;

public class UserCommandInterpreter extends CommandInterpreter<UserInfoResponse> {

    private final IUserService userService;

    private final ISearch<String, User> userSearch;

    private final ITournamentHelper tournamentHelper;

    public UserCommandInterpreter(IUserService userService,
            ISearch<String, User> userSearch,
            ITournamentHelper tournamentHelper,
            ICommandParsedDataProvider parsedDataProvider) {
        super(parsedDataProvider);
        this.userService = userService;
        this.userSearch = userSearch;
        this.tournamentHelper = tournamentHelper;
    }

    @Override
    UserInfoResponse getResponse() {
        List<User> users = new ArrayList<>();

        if (isParameterExists("id", "i")) {
            users.add(userService.findUserById(getParameterValue("id", "i").orElse(null)).orElse(null));
        } else if (isParameterExists("query", "q")) {
            users.addAll(userSearch.find(getParameterValue("query", "q").orElse(null)));
        }

        var response = new UserInfoResponse();
        response.setItems(users.stream().map(user -> {
                    var item = new UserInfoResponseItem();
                    item.setId(user.getId());
                    if (isFlagExists("name", "n")) {
                        item.setName(user.getName());
                    }

                    if (isFlagExists("surname", "s")) {
                        item.setSurname(user.getSurname());
                    }

                    if (isFlagExists("fullname", "fn")) {
                        item.setFullName(user.getFullName());
                    }

                    if (isFlagExists("email", "e")) {
                        item.setEmail(user.getUsername());
                    }

                    if (isFlagExists("tournament", "t")) {
                        item.setTournaments(tournamentHelper.getUserTournaments(user.getId()).stream()
                                .map(Persistent::getId)
                                .collect(Collectors.toList()));
                    }
                    return item;
                })
                        .collect(Collectors.toList())

        );
        return response;
    }
}
