package pl.wturnieju.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.cli.dto.UserInfoResponse;
import pl.wturnieju.cli.dto.UserInfoResponseItem;
import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.inserter.TournamentInserter;
import pl.wturnieju.inserter.UserInserter;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.search.ISearch;
import pl.wturnieju.search.UserSimpleSearchService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentCreatorService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.impl.TournamentCreatorService;
import pl.wturnieju.service.impl.TournamentService;
import pl.wturnieju.service.impl.UserService;

@Import(value = MongoConfig.class)
@ExtendWith(SpringExtension.class)
@DataMongoTest
@EnableAutoConfiguration
@WithMockCustomUser(username = "aukjan@yahoo.com")
public class UserCommandInterpreterTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ApplicationContext context;

    private IUserService userService;

    private ITournamentService tournamentService;

    private ITournamentCreatorService tournamentCreatorService;

    private IParticipantService tournamentParticipantService;

    private ISearch<String, User> userSearch;

    private String testedUserId;

    private List<String> userTournamentsIds;

    @BeforeEach
    public void setUp() throws Exception {
        setUpService();
        setUpInserter();
        userSearch = new UserSimpleSearchService(userRepository);
        testedUserId = userRepository.findAll().stream().skip(5).findFirst().map(Persistent::getId).orElseThrow();

        inviteUserToTournaments();
    }

    private void inviteUserToTournaments() {
        userTournamentsIds = tournamentRepository.findAll().stream()
                .limit(3)
                .map(Persistent::getId)
                .collect(Collectors.toList());

        userTournamentsIds.forEach(
                tournamentId -> tournamentParticipantService.inviteUserId(tournamentId, testedUserId));
    }

    private void setUpInserter() {
        new UserInserter(userService, userRepository).insertUsersToDatabase();
        new TournamentInserter(tournamentCreatorService).insertTournamentToDatabase();
    }

    private void setUpService() {
        userService = new UserService(new BCryptPasswordEncoder(), userRepository);
        tournamentService = new TournamentService(tournamentRepository, context);
        tournamentCreatorService = new TournamentCreatorService(tournamentRepository, userService);
        //        tournamentParticipantService = new ParticipantService(tournamentService, userService);
    }

    @Test
    public void getResponseWithEmail() {
        var command1 = String.format("user --id=%s --email", testedUserId);
        var command2 = String.format("user -i=%s -e", testedUserId);

        var response1 = createInitializedInterpreterByCommand(command1).getResponse();
        var response2 = createInitializedInterpreterByCommand(command2).getResponse();

        var expectedResponse = new UserInfoResponse();
        var item = new UserInfoResponseItem();
        item.setId(testedUserId);
        item.setEmail(userService.findUserById(testedUserId).map(User::getUsername).orElseThrow());
        expectedResponse.setItems(Collections.singletonList(item));

        assertEquals(expectedResponse, response1);
        assertEquals(expectedResponse, response2);
    }

    @Test
    public void getResponseWithName() {
        var command1 = String.format("user --id=%s --name", testedUserId);
        var command2 = String.format("user -i=%s -n", testedUserId);

        var response1 = createInitializedInterpreterByCommand(command1).getResponse();
        var response2 = createInitializedInterpreterByCommand(command2).getResponse();

        var expectedResponse = new UserInfoResponse();
        var item = new UserInfoResponseItem();
        item.setId(testedUserId);
        item.setName(userService.findUserById(testedUserId).map(User::getName).orElseThrow());
        expectedResponse.setItems(Collections.singletonList(item));

        assertEquals(expectedResponse, response1);
        assertEquals(expectedResponse, response2);
    }

    @Test
    public void getResponseWithSurname() {
        var command1 = String.format("user --id=%s --surname", testedUserId);
        var command2 = String.format("user -i=%s -s", testedUserId);

        var response1 = createInitializedInterpreterByCommand(command1).getResponse();
        var response2 = createInitializedInterpreterByCommand(command2).getResponse();

        var expectedResponse = new UserInfoResponse();
        var item = new UserInfoResponseItem();
        item.setId(testedUserId);
        item.setSurname(userService.findUserById(testedUserId).map(User::getSurname).orElseThrow());
        expectedResponse.setItems(Collections.singletonList(item));

        assertEquals(expectedResponse, response1);
        assertEquals(expectedResponse, response2);
    }

    @Test
    public void getResponseWithFullName() {
        var command1 = String.format("user --id=%s --fullname", testedUserId);
        var command2 = String.format("user -i=%s -fn", testedUserId);

        var response1 = createInitializedInterpreterByCommand(command1).getResponse();
        var response2 = createInitializedInterpreterByCommand(command2).getResponse();

        var expectedResponse = new UserInfoResponse();
        var item = new UserInfoResponseItem();
        item.setId(testedUserId);
        item.setFullName(userService.findUserById(testedUserId).map(User::getFullName).orElseThrow());
        expectedResponse.setItems(Collections.singletonList(item));

        assertEquals(expectedResponse, response1);
        assertEquals(expectedResponse, response2);
    }

    @Test
    public void getResponseWithTournaments() {
        var command1 = String.format("user --id=%s --tournament", testedUserId);
        var command2 = String.format("user -i=%s -t", testedUserId);

        var response1 = createInitializedInterpreterByCommand(command1).getResponse();
        var response2 = createInitializedInterpreterByCommand(command2).getResponse();

        var expectedResponse = new UserInfoResponse();
        var item = new UserInfoResponseItem();
        item.setId(testedUserId);
        item.setTournaments(userTournamentsIds);
        expectedResponse.setItems(Collections.singletonList(item));

        assertEquals(expectedResponse, response1);
        assertEquals(expectedResponse, response2);
    }

    @Test
    public void getUsersById() {
        var command1 = "user --id=" + testedUserId;
        var command2 = "user -i=" + testedUserId;

        var interpreter1 = createInitializedInterpreterByCommand(command1);
        var interpreter2 = createInitializedInterpreterByCommand(command2);

        var expectedResponse = new UserInfoResponse();
        var item = new UserInfoResponseItem();
        item.setId(testedUserId);
        expectedResponse.setItems(Collections.singletonList(item));

        var response1 = interpreter1.getResponse();
        var response2 = interpreter2.getResponse();

        assertEquals(expectedResponse, response1);
        assertEquals(expectedResponse, response2);
    }

    @Test
    public void getUsersByQuery() {
        var testedUserFullName = userRepository.findById(testedUserId).map(User::getFullName).orElseThrow();
        var command1 = "user --query=" + testedUserFullName;
        var command2 = "user -q=" + testedUserFullName;


        var interpreter1 = createInitializedInterpreterByCommand(command1);
        var interpreter2 = createInitializedInterpreterByCommand(command2);

        var expectedResponse = new UserInfoResponse();
        var item = new UserInfoResponseItem();
        item.setId(testedUserId);
        expectedResponse.setItems(Collections.singletonList(item));

        var response1 = interpreter1.getResponse();
        var response2 = interpreter2.getResponse();

        assertEquals(expectedResponse, response1);
        assertEquals(expectedResponse, response2);
    }

    @AfterEach
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        tournamentRepository.deleteAll();
    }

    private UserCommandInterpreter createInitializedInterpreterByCommand(String command) {
        var provider = createInitializedParsedDataProvider(command);
        return createInitializedUserCommandInterpreter(provider);
    }

    private ICommandParsedDataProvider createInitializedParsedDataProvider(String command) {
        try {
            var provider = new CliCommandParser(command);
            provider.parse();
            return provider;
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        return null;
    }

    private UserCommandInterpreter createInitializedUserCommandInterpreter(ICommandParsedDataProvider parsedDataProvider) {
        return (UserCommandInterpreter) CommandInterpreterFactory.createInterpreter(userService, tournamentService,
                userSearch, null, parsedDataProvider);
    }
}