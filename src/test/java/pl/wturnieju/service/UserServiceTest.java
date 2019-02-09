package pl.wturnieju.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.config.AuthorityType;
import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.config.user.AuthConfiguration;
import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.exception.IncorrectPasswordException;
import pl.wturnieju.exception.InvalidFormatException;
import pl.wturnieju.exception.ResourceExistsException;
import pl.wturnieju.exception.UserNotFoundException;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.User;
import pl.wturnieju.model.UserGrantedAuthority;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.impl.UserService;
import pl.wturnieju.service.impl.ValidatorService;


@ExtendWith(SpringExtension.class)
@Import(value = {MongoConfig.class, AuthConfiguration.class, ValidatorService.class})
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

    private static final String basePassword = "Password123.";

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    private IUserService userService;

    private User savedActiveUser;

    private User savedInactiveUser;

    @Autowired
    private IValidatorService validatorService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(encoder, userRepository, validatorService);
        createSavedUser();
        insertUser();
    }

    private void createSavedUser() {
        savedActiveUser = User.builder()
                .username("email@email.com")
                .password(encoder.encode(basePassword))
                .enabled(true)
                .build();

        savedInactiveUser = User.builder()
                .username("email2@email.com")
                .password(encoder.encode(basePassword))
                .enabled(false)
                .build();
    }

    @Test
    public void loadUserByUsernameShouldFoundUserTest() {
        assertNotNull(userService.loadUserByUsername(savedActiveUser.getUsername()));
    }

    @Test
    public void loadUserByUsernameShouldNotFoundUserTest() {
        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(savedActiveUser.getUsername() + 1));
    }

    @Test
    public void createUserShouldPass() {
        try {
            userService.create("a" + savedActiveUser.getUsername(), basePassword);
        } catch (Exception e) {
            fail();
        }
        assertTrue(userRepository.findByUsername("a" + savedActiveUser.getUsername()).isPresent());
    }

    @Test
    public void createUserShouldFailBecauseExists() {
        assertThrows(ResourceExistsException.class,
                () -> userService.create(savedActiveUser.getUsername(), basePassword));
    }

    @Test
    public void createUserShouldFailBecauseEmailValidationFail() {
        badEmails.forEach(
                email -> assertThrows(InvalidFormatException.class, () -> userService.create(email, basePassword)));
    }

    @Test
    public void passwordEncodingTestShouldPass() {
        var rawPass = basePassword + 1;
        var email = "a" + savedActiveUser.getUsername();
        userService.create(email, rawPass);
        var user = userRepository.findByUsername(email);
        assertTrue(encoder.matches(rawPass, user.map(User::getPassword).orElse(null)));
    }

    @Test
    public void newUserShouldBeDisabled() {
        var email = "a" + savedActiveUser.getUsername();
        var password = "a" + basePassword;
        userService.create(email, password);
        var user = userRepository.findByUsername(email).orElseThrow();
        assertFalse(user.isEnabled());
    }

    @Test
    public void confirmNewAccountShouldPass() {
        var email = "a" + savedActiveUser.getUsername();
        var password = "a" + basePassword;

        userService.create(email, password);
        userService.confirmNewAccount(email);

        var user = userRepository.findByUsername(email).orElseThrow();
        assertTrue(user.isEnabled());
    }

    @Test
    public void confirmEmailChangeShouldPass() {
        var email = "a" + savedActiveUser.getUsername();
        userService.confirmChangedEmail(savedActiveUser.getUsername(), email);
        assertTrue(userRepository.findByUsername(email).isPresent());
    }

    @Test
    public void validateEmailChangePassTest() {
        var email = "a" + savedActiveUser.getUsername();
        userService.validateEmailChange(email, basePassword);
    }

    @Test
    public void validateEmailChangeShouldFailByFormatTest() {
        badEmails.forEach(email -> assertThrows(InvalidFormatException.class,
                () -> userService.validateEmailChange(email, "ignore")));
    }

    @Test
    public void validateEmailChangeShouldFailByIncorrectPasswordTest() {
        var email = "a" + savedActiveUser.getUsername();
        assertThrows(IncorrectPasswordException.class, () -> userService.validateEmailChange(email, basePassword + 1));
    }

    private void insertUser() {
        userRepository.insert(savedActiveUser);
        userRepository.insert(savedInactiveUser);
    }

    @Test
    public void checkCredentialsShouldPass() {
        assertTrue(userService.checkCredentials(savedActiveUser.getUsername(), basePassword));
    }

    @Test
    public void checkCredentialsShouldFailByWrongUsername() {
        assertFalse(userService.checkCredentials("a" + savedActiveUser.getUsername(), basePassword));
    }

    @Test
    public void checkCredentialsShouldFailByWrongPass() {
        assertFalse(userService.checkCredentials(savedActiveUser.getUsername(), "a" + basePassword));
    }

    @Test
    public void changePasswordShouldPass() {
        var changedPassword = basePassword + "a";
        userService.changePassword(savedActiveUser.getUsername(), changedPassword, basePassword);
        assertTrue(userService.checkCredentials(savedActiveUser.getUsername(), changedPassword));
    }

    @Test
    public void changePasswordShouldFailByNonExistingUsername() {
        var changedPassword = basePassword + "a";
        assertThrows(UserNotFoundException.class,
                () -> userService.changePassword("a" + savedActiveUser.getUsername(), changedPassword, basePassword));
    }

    @Test
    public void changePasswordShouldFailByBadPasswordFormat() {
        var changedPassword = "a";
        assertThrows(InvalidFormatException.class,
                () -> userService.changePassword(savedActiveUser.getUsername(), changedPassword, basePassword));
    }

    @Test
    public void setAuthoritiesShouldPass() {
        var authorities = Collections.singleton(new UserGrantedAuthority(AuthorityType.CLI));
        userService.setAuthorities(authorities);
        var user = userRepository.findByUsername(savedActiveUser.getUsername()).orElseThrow();
        assertEquals(authorities, user.getAuthorities());
    }

    @Test
    public void getByProfileShouldPass() {
        var profile = (IProfile) savedActiveUser;
        var user = userService.findUseByProfile(profile).orElseThrow();
        assertEquals(savedActiveUser, user);
    }

    @Test
    public void isAccountActiveShouldReturnFalseWhenInactive() {
        var username = savedInactiveUser.getUsername();
        assertFalse(userService.isAccountActive(username));
    }

    @Test
    public void isAccountActiveShouldThrowWhenUserNotFound() {
        var username = savedActiveUser.getUsername() + 1;
        assertThrows(ResourceNotFoundException.class, () -> userService.isAccountActive(username));
    }

    @Test
    public void isAccountActiveShouldReturnTrueWhenActive() {
        var username = savedActiveUser.getUsername();
        assertTrue(userService.isAccountActive(username));
    }

    @Test
    public void resetPasswordShouldPass() {
        var newPass = basePassword + 1;
        var username = savedActiveUser.getUsername();
        userService.resetPassword(username, newPass);
        var user = userRepository.findByUsername(username).orElseThrow();
        assertTrue(encoder.matches(newPass, user.getPassword()));
    }

    @Test
    public void resetPasswordShouldFailByBadFormat() {
        var badPass = "aaa";
        var username = savedActiveUser.getUsername();

        assertThrows(InvalidFormatException.class, () -> userService.resetPassword(username, badPass));
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

}