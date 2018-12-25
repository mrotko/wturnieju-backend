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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.exception.IncorrectPasswordException;
import pl.wturnieju.exception.InvalidFormatException;
import pl.wturnieju.exception.ResourceExistsException;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.UserRepository;

//@SpringBootTest
@RunWith(SpringRunner.class)
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

    private static final String password = "Password123,";

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    private IUserService userService;

    private User notSavedUser;

    private User savedUser;


    @Before
    public void setUp() {
        userRepository.deleteAll();
        userService = new UserService(encoder, userRepository);
        createSavedUser();
        createNotSavedUser();
        insertUser();
    }

    private void createSavedUser() {
        savedUser = User.builder()
                .username(usernameIn)
                .password(encoder.encode(password))
                .build();
    }

    private void createNotSavedUser() {
        notSavedUser = User.builder()
                .username("a" + usernameIn)
                .password(password)
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
            userService.create("a" + usernameIn, password);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(userRepository.findByUsername("a" + usernameIn).isPresent());
    }

    @Test(expected = ResourceExistsException.class)
    public void createUserShouldFailBecauseExists() {
        userService.create(usernameIn, password);
    }

    @Test(expected = InvalidFormatException.class)
    public void createUserShouldFailBecauseEmailValidationFail() {
        badEmails.forEach(email -> userService.create(email, password));
    }

    @Test
    public void passwordEncodingTestShouldSuccess() {
        var rawPass = password + 1;
        var email = "a" + usernameIn;
        userService.create(email, rawPass);
        var user = userRepository.findByUsername(email);
        Assert.assertTrue(encoder.matches(rawPass, user.map(User::getPassword).orElse(null)));
    }

    @Test
    public void changeEmailSuccessTest() {
        var email = "a" + savedUser.getUsername();
        userService.changeEmail(email, password);
        Assert.assertTrue(userRepository.findByUsername(email).isPresent());
        Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test(expected = InvalidFormatException.class)
    public void changeEmailShouldFailByFormatTest() {
        badEmails.forEach(email -> userService.changeEmail(email, "ignore"));
    }

    @Test(expected = IncorrectPasswordException.class)
    public void changeEmailShouldFailByInvalidPasswordTest() {
        var email = "a" + usernameIn;
        userService.changeEmail(email, password + 1);
    }

    private void insertUser() {
        userRepository.insert(savedUser);
    }

    @Test
    public void checkCredentialsShouldPass() {
        Assert.assertTrue(userService.checkCredentials(usernameIn, password));
    }

    @Test
    public void checkCredentialsShouldFailByWrongUsername() {
        Assert.assertFalse(userService.checkCredentials("a" + usernameIn, password));
    }

    @Test
    public void checkCredentialsShouldFailByWrongPass() {
        Assert.assertFalse(userService.checkCredentials(usernameIn, "a" + password));
    }

    @Test
    public void changePasswordShouldPass() {
        var changedPassword = password + "a";
        userService.changePassword(usernameIn, changedPassword, password);
        Assert.assertTrue(userService.checkCredentials(usernameIn, changedPassword));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void changePasswordShouldFailByNonExistingUsername() {
        var changedPassword = password + "a";
        userService.changePassword("a" + usernameIn, changedPassword, password);
    }

    @Test(expected = InvalidFormatException.class)
    public void changePasswordShouldFailByBadPasswordFormat() {
        var changedPassword = "a";
        userService.changePassword(usernameIn, changedPassword, password);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }
}