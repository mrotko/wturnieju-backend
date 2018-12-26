package pl.wturnieju.cli;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.cli.dto.SettingsInfoResponse;
import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.inserter.UserInserter;
import pl.wturnieju.model.ChangeEmailVerificationToken;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.TokenVerificationRepository;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.EmailChangeTokenVerificationService;
import pl.wturnieju.service.IEmailService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;
import pl.wturnieju.service.UserService;

@Import(value = MongoConfig.class)
@RunWith(SpringRunner.class)
@DataMongoTest
@EnableAutoConfiguration
@WithMockCustomUser(username = "aukjan@yahoo.com")
public class SettingsCommandInterpreterTest {

    private User currentUser;

    private String rawPassword = "EFcYcdFXT7GCAa1,";

    private IUserService userService;

    private IVerificationService<ChangeEmailVerificationToken> verificationService;

    @Mock
    private IEmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenVerificationRepository verificationRepository;

    @Before
    public void setUp() {
        userService = new UserService(new BCryptPasswordEncoder(), userRepository);
        verificationService = new EmailChangeTokenVerificationService(verificationRepository, emailService);
        new UserInserter(userService, userRepository).insertUsersToDatabase();
        currentUser = userService.getCurrentUser();
    }

    @Test
    public void changeEmailTest() {
        var email = "aukjan1@yahoo.com";

        var expectedUser = userService.getCurrentUser();
        expectedUser.setUsername(email);
        var interpreter = createCommandInterpreterForCommand(
                "settings --email=" + email + " --password=" + rawPassword);

        var expectedResponse = new SettingsInfoResponse();
        expectedResponse.setEmail(email);
        var response = interpreter.getResponse();

        Assert.assertEquals(expectedResponse, response);

        var token = verificationService.getValidToken(getFirstToken());
        Assert.assertEquals(email, token.getNewEmail());
    }

    @Test
    public void changeEmailAliasTest() {
        var email = "aukjan2@yahoo.com";

        var expectedUser = userService.getCurrentUser();
        expectedUser.setUsername(email);
        var interpreter = createCommandInterpreterForCommand("settings -e=" + email + " -p=" + rawPassword);

        var expectedResponse = new SettingsInfoResponse();
        expectedResponse.setEmail(email);
        var response = interpreter.getResponse();

        Assert.assertEquals(expectedResponse, response);

        var token = verificationService.getValidToken(getFirstToken());
        Assert.assertEquals(email, token.getNewEmail());
    }

    @Test
    public void getEmailTest() {
        var flagNameAndAlias = Arrays.asList("--email", "-e");
        getValueTask(flagNameAndAlias, settingsInfoResponse ->
                settingsInfoResponse.setEmail(userService.getCurrentUser().getUsername()));
    }

    private String getFirstToken() {
        var tokens = verificationRepository.findAll();
        if (tokens.isEmpty()) {
            return null;
        }
        return tokens.get(0).getToken();
    }

    @Test
    public void changePasswordTest() {
        var pass1 = "Asdfgh123.";
        var oldPassword = rawPassword;
        var pass2 = "Asdfgh1234.";

        var command1 = "settings --password=" + pass1 + " --oldPassword=" + oldPassword;
        var command2 = "settings -p=" + pass2 + " -o=" + pass1;

        var parser1 = createInitializedCommandParser(command1);
        var parser2 = createInitializedCommandParser(command2);

        var interpreter1 = createInitializedSettingsCommandInterpreter(parser1);
        var interpreter2 = createInitializedSettingsCommandInterpreter(parser2);

        var response1 = (SettingsInfoResponse) interpreter1.getResponse();
        Assert.assertNull(response1.getErrorMessages());
        Assert.assertTrue(response1.getPasswordChanged());
        Assert.assertTrue(userService.checkCredentials(currentUser.getUsername(), pass1));


        var response2 = (SettingsInfoResponse) interpreter2.getResponse();
        Assert.assertNull(response2.getErrorMessages());
        Assert.assertTrue(response2.getPasswordChanged());
        Assert.assertTrue(userService.checkCredentials(currentUser.getUsername(), pass2));
    }

    @Test
    public void changePasswordWithEqInsideTest() {
        var pass = "Asdfgh123.=";
        var oldPass = rawPassword;
        var command1 = "settings --password=" + pass + " --oldPassword=" + oldPass;
        var parser1 = createInitializedCommandParser(command1);
        var response = createInitializedSettingsCommandInterpreter(parser1).getResponse();

        Assert.assertNull(response.getErrorMessages());
        Assert.assertTrue(response.getPasswordChanged());
        Assert.assertTrue(userService.checkCredentials(currentUser.getUsername(), pass));
    }

    @Test
    public void changePasswordWithEqQuoteInsideTest() {
        var pass = "Asdfgh123.=\"";
        var oldPass = rawPassword;
        var command1 = "settings --password=" + pass + " --oldPassword=" + oldPass;
        var parser1 = createInitializedCommandParser(command1);
        var response = createInitializedSettingsCommandInterpreter(parser1).getResponse();

        Assert.assertNull(response.getErrorMessages());
        Assert.assertTrue(response.getPasswordChanged());
        Assert.assertTrue(userService.checkCredentials(currentUser.getUsername(), pass));
    }

    @Test
    public void setNameTest() {
        var name1 = "Alisson";
        var name2 = "Becker";


        var user = userService.getCurrentUser();
        user.setName(name1);
        setValueTask("--name=" + name1, settingsInfoResponse -> settingsInfoResponse.setName(name1), user);


        user.setName(name2);
        setValueTask("-n=" + name2, settingsInfoResponse -> settingsInfoResponse.setName(name2), user);
    }

    @Test
    public void getNameTest() {
        var flagNameAndAlias = Arrays.asList("--name", "-n");
        getValueTask(flagNameAndAlias, settingsInfoResponse ->
                settingsInfoResponse.setName(userService.getCurrentUser().getName()));
    }

    @Test
    public void setSurnameTest() {
        var surname1 = "Alisson";
        var surname2 = "Becker";

        var user = userService.getCurrentUser();
        user.setSurname(surname1);
        setValueTask("--surname=" + surname1, settingsInfoResponse -> settingsInfoResponse.setSurname(surname1), user);

        user.setSurname(surname2);
        setValueTask("-s=" + surname2, settingsInfoResponse -> settingsInfoResponse.setSurname(surname2), user);
    }

    @Test
    public void setSurnameWithSpaceBetweenTest() {
        var surname = "Alisson Becker";
        var user = userService.getCurrentUser();
        user.setSurname(surname);
        setValueTask("--surname=" + "\"" + surname + "\"",
                settingsInfoResponse -> settingsInfoResponse.setSurname(surname), user);
    }

    @Test
    public void getSurnameTest() {
        var flagNameAndAlias = Arrays.asList("--surname", "-s");
        getValueTask(flagNameAndAlias, settingsInfoResponse ->
                settingsInfoResponse.setSurname(userService.getCurrentUser().getSurname()));
    }

    @Test
    public void getFullNameTest() {
        var flagNameAndAlias = Arrays.asList("--fullname", "-fn");
        getValueTask(flagNameAndAlias, settingsInfoResponse ->
                settingsInfoResponse.setFullName(userService.getCurrentUser().getFullName()));
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
        verificationRepository.deleteAll();
    }

    private CliCommandParser createInitializedCommandParser(String command) {
        var parser = new CliCommandParser(command);
        try {
            parser.parse();
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        return parser;
    }

    private SettingsCommandInterpreter createInitializedSettingsCommandInterpreter(ICommandParsedDataProvider parsedDataProvider) {
        return (SettingsCommandInterpreter) CommandInterpreterFactory.createInterpreter(userService, null,
                null, verificationService, parsedDataProvider);
    }

    private SettingsCommandInterpreter createCommandInterpreterForCommand(String command) {
        var parser = createInitializedCommandParser(command);
        return createInitializedSettingsCommandInterpreter(parser);
    }


    private void getValueTask(List<String> flagNameAndAlias, Consumer<SettingsInfoResponse> expectedValue) {
        flagNameAndAlias.forEach(f -> {
            var interpreter = createCommandInterpreterForCommand(String.format("settings %s", f));
            var interpreterResponse = interpreter.getResponse();
            var expectedResponse = new SettingsInfoResponse();
            expectedValue.accept(expectedResponse);
            Assert.assertEquals(expectedResponse, interpreterResponse);
        });
    }

    private void setValueTask(String parameter,
            Consumer<SettingsInfoResponse> expectedResponseValue,
            User expectedUser) {
        var interpreter = createCommandInterpreterForCommand(String.format("settings %s", parameter));
        var response = interpreter.getResponse();

        var expectedResponse = new SettingsInfoResponse();
        expectedResponseValue.accept(expectedResponse);
        Assert.assertEquals(expectedResponse, response);

        var user = userService.getCurrentUser();
        Assert.assertEquals(expectedUser, user);
    }
}