package pl.wturnieju.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.model.verification.NewAccountVerificationData;
import pl.wturnieju.model.verification.NewAccountVerificationToken;
import pl.wturnieju.repository.TokenVerificationRepository;
import pl.wturnieju.service.impl.verification.NewAccountTokenVerificationService;

@ExtendWith(SpringExtension.class)
@Import(value = MongoConfig.class)
@DataMongoTest
@EnableAutoConfiguration
public class NewAccountTokenVerificationServiceTest {

    @Autowired
    private TokenVerificationRepository<NewAccountVerificationToken> verificationRepository;

    @Mock
    private IEmailService emailService;

    private IVerificationService<NewAccountVerificationToken> verificationService;

    @BeforeEach
    public void setUp() throws Exception {
        verificationService = new NewAccountTokenVerificationService(verificationRepository, emailService);
    }

    @AfterEach
    public void tearDown() throws Exception {
        verificationRepository.deleteAll();
    }

    @Test
    public void createVerification() {
        var data = new NewAccountVerificationData();
        var email = "admin@admin.com";

        data.setEmail(email);
        verificationService.createVerificationToken(data);

        var token = verificationService.getValidToken(getFirstToken());

        assertNotNull(token);
        assertEquals(email, token.getEmail());
    }

    private String getFirstToken() {
        var tokens = verificationRepository.findAll();
        if (tokens.isEmpty()) {
            return null;
        }
        return tokens.get(0).getToken();
    }
}