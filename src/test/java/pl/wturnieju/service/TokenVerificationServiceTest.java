package pl.wturnieju.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.repository.TokenVerificationRepository;
import pl.wturnieju.repository.UserRepository;

@RunWith(SpringRunner.class)
@Import(value = MongoConfig.class)
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

    //    private IVerificationService verificationService;

    private IUserService userService;

    @Before
    public void setUp() throws Exception {
        userService = new UserService(passwordEncoder, userRepository);
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


    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        verificationRepository.deleteAll();
    }


}