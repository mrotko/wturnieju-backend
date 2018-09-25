package pl.wturnieju.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.UserRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@DataMongoTest
@EnableAutoConfiguration
@WithMockCustomUser(username = "email@email.com")
public class UserServiceTest {

    private final String usernameIn = "email@email.com";

    private final String password = "Password123,";

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
                .username(usernameIn + 1)
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

    @Test(expected = ValidationException.class)
    public void createUserShouldFailBecauseExists() throws ValidationException {
        userService.create(usernameIn, password);
    }

    @Test()
    public void createUserShouldFailBecauseEmailValidationFail() {
        List<String> emails = Arrays.asList(
                "",
                "email",
                "@email",
                "email@",
                "email@.com",
                "@.com",
                "@test.com"
        );

        emails.forEach(email -> {
            boolean failed = false;
            try {
                userService.create(email, password);
            } catch (ValidationException e) {
                failed = true;
            }
            Assert.assertTrue(failed);
        });
    }

    @Test
    public void passwordEncodingTestShouldSuccess() throws ValidationException {
        var rawPass = password + 1;
        var email = "a" + usernameIn;
        userService.create(email, rawPass);
        var user = userRepository.findByUsername(email);
        Assert.assertTrue(encoder.matches(rawPass, user.map(User::getPassword).orElse(null)));
    }

    @Test
    public void changeEmailSuccessTest() {
        var email = savedUser.getUsername() + 1;
        userService.changeEmail(email);
        Assert.assertTrue(userRepository.findByUsername(email).isPresent());
        Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private void insertUser() {
        userRepository.insert(savedUser);
    }
}