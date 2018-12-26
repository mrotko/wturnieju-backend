package pl.wturnieju.service;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.exception.IncorrectPasswordException;
import pl.wturnieju.exception.InvalidFormatException;
import pl.wturnieju.exception.ResourceExistsException;
import pl.wturnieju.exception.UserNotFoundException;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.UserRepository;


@RunWith(SpringRunner.class)
@Import(value = MongoConfig.class)
@DataMongoTest
@EnableAutoConfiguration
@WithMockCustomUser(username = "email@email.com")
public class UserServiceTest {

    private static final List<String> badEmails = Arrays.asList(
            "",
            "email",
            "@email",
            "email@",
            "email@.com",
            "@.com",
            "@test.com"
    );
    private static final String usernameIn = "email@email.com";

    private static final String basePassword = "Password123,";

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    private IUserService userService;

    private User savedUser;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        userService = new UserService(encoder, userRepository);
        createSavedUser();
        insertUser();
    }

    private void createSavedUser() {
        savedUser = User.builder()
                .username(usernameIn)
                .password(encoder.encode(basePassword))
                .build();
    }

    @Test
    public void loadUserByUsernameShouldFoundUserTest() {
        Assert.assertNotNull(userService.loadUserByUsername(usernameIn));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameShouldNotFoundUserTest() {
        userService.loadUserByUsername(usernameIn + 1);
    }

    @Test
    public void createUserShouldPass() {
        try {
            userService.create("a" + usernameIn, basePassword);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(userRepository.findByUsername("a" + usernameIn).isPresent());
    }

    @Test(expected = ResourceExistsException.class)
    public void createUserShouldFailBecauseExists() {
        userService.create(usernameIn, basePassword);
    }

    @Test(expected = InvalidFormatException.class)
    public void createUserShouldFailBecauseEmailValidationFail() {
        badEmails.forEach(email -> userService.create(email, basePassword));
    }

    @Test
    public void passwordEncodingTestShouldPass() {
        var rawPass = basePassword + 1;
        var email = "a" + usernameIn;
        userService.create(email, rawPass);
        var user = userRepository.findByUsername(email);
        Assert.assertTrue(encoder.matches(rawPass, user.map(User::getPassword).orElse(null)));
    }

    @Test
    public void newUserShouldBeDisabled() {
        var email = "a" + usernameIn;
        var password = "a" + basePassword;
        userService.create(email, password);
        var user = userRepository.findByUsername(email).orElseThrow();
        Assert.assertFalse(user.isEnabled());
    }

    @Test
    public void confirmNewAccountShouldPass() {
        var email = "a" + usernameIn;
        var password = "a" + basePassword;

        userService.create(email, password);
        userService.confirmNewAccount(email);

        var user = userRepository.findByUsername(email).orElseThrow();
        Assert.assertTrue(user.isEnabled());
    }

    @Test
    public void confirmEmailChangeShouldPass() {
        var email = "a" + savedUser.getUsername();
        userService.confirmChangedEmail(savedUser.getUsername(), email);
        Assert.assertTrue(userRepository.findByUsername(email).isPresent());
    }

    @Test
    public void validateEmailChangePassTest() {
        var email = "a" + savedUser.getUsername();
        userService.validateEmailChange(email, basePassword);
    }

    @Test(expected = InvalidFormatException.class)
    public void validateEmailChangeShouldFailByFormatTest() {
        badEmails.forEach(email -> userService.validateEmailChange(email, "ignore"));
    }

    @Test(expected = IncorrectPasswordException.class)
    public void validateEmailChangeShouldFailByIncorrectPasswordTest() {
        var email = "a" + usernameIn;
        userService.validateEmailChange(email, basePassword + 1);
    }

    private void insertUser() {
        userRepository.insert(savedUser);
    }

    @Test
    public void checkCredentialsShouldPass() {
        Assert.assertTrue(userService.checkCredentials(usernameIn, basePassword));
    }

    @Test
    public void checkCredentialsShouldFailByWrongUsername() {
        Assert.assertFalse(userService.checkCredentials("a" + usernameIn, basePassword));
    }

    @Test
    public void checkCredentialsShouldFailByWrongPass() {
        Assert.assertFalse(userService.checkCredentials(usernameIn, "a" + basePassword));
    }

    @Test
    public void changePasswordShouldPass() {
        var changedPassword = basePassword + "a";
        userService.changePassword(usernameIn, changedPassword, basePassword);
        Assert.assertTrue(userService.checkCredentials(usernameIn, changedPassword));
    }

    @Test(expected = UserNotFoundException.class)
    public void changePasswordShouldFailByNonExistingUsername() {
        var changedPassword = basePassword + "a";
        userService.changePassword("a" + usernameIn, changedPassword, basePassword);
    }

    @Test(expected = InvalidFormatException.class)
    public void changePasswordShouldFailByBadPasswordFormat() {
        var changedPassword = "a";
        userService.changePassword(usernameIn, changedPassword, basePassword);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }
}