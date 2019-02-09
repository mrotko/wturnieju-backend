package pl.wturnieju.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.config.user.AuthConfiguration;
import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.repository.TokenVerificationRepository;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.impl.UserService;
import pl.wturnieju.service.impl.ValidatorService;

@ExtendWith(SpringExtension.class)
@Import(value = {MongoConfig.class, AuthConfiguration.class, ValidatorService.class})
@DataMongoTest
@EnableAutoConfiguration
@WithMockCustomUser(username = "email@email.com")
public class TokenVerificationServiceTest {

    private static final String BASE_EMAIL = "email@email.com";
    private static final String BASE_PASSWORD = "Password123,";

    @Autowired
    private TokenVerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private IEmailService emailService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private ValidatorService validatorService;


    //    private IVerificationService verificationService;

    private IUserService userService;

    @BeforeEach
    public void setUp() throws Exception {
        userService = new UserService(passwordEncoder, userRepository, validatorService);
        //        verificationService = new TokenVerificationService(verificationRepository, emailService);
    }


    @Test
    public void verifyNewAccountShouldPass() {


    }

    @Test
    public void verifyEmailChangeShouldPass() {

    }

    @Test
    public void createVerificationForNewAccountShouldPass() {
        var email = BASE_EMAIL;
        var pass = BASE_PASSWORD;

        userService.create(email, pass);

    }

    @Test
    public void createVerificationForEmailChangeShouldPass() {

    }


    @AfterEach
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        verificationRepository.deleteAll();
    }


}