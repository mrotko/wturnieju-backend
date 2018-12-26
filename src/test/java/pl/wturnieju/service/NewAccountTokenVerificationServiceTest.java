package pl.wturnieju.service;

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
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.model.NewAccountVerificationToken;
import pl.wturnieju.repository.TokenVerificationRepository;

@RunWith(SpringRunner.class)
@Import(value = MongoConfig.class)
@DataMongoTest
@EnableAutoConfiguration
public class NewAccountTokenVerificationServiceTest {

    @Autowired
    private TokenVerificationRepository verificationRepository;

    @Mock
    private IEmailService emailService;

    private IVerificationService<NewAccountVerificationToken> verificationService;

    @Before
    public void setUp() throws Exception {
        verificationService = new NewAccountTokenVerificationService(verificationRepository, emailService);
    }

    @After
    public void tearDown() throws Exception {
        verificationRepository.deleteAll();
    }

    @Test
    public void createVerification() {
        var data = new NewAccountVerificationData();
        var email = "admin@admin.com";

        data.setEmail(email);
        verificationService.createVerification(data);

        var token = verificationService.getValidToken(getFirstToken());

        Assert.assertNotNull(token);
        Assert.assertEquals(email, token.getEmail());
    }

    private String getFirstToken() {
        var tokens = verificationRepository.findAll();
        if (tokens.isEmpty()) {
            return null;
        }
        return tokens.get(0).getToken();
    }
}