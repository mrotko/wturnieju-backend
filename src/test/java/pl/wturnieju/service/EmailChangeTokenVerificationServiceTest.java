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
import pl.wturnieju.model.verification.ChangeEmailVerificationToken;
import pl.wturnieju.model.verification.EmailChangeVerificationData;
import pl.wturnieju.repository.TokenVerificationRepository;
import pl.wturnieju.service.impl.verification.EmailChangeTokenVerificationService;

@ExtendWith(SpringExtension.class)
@Import(value = MongoConfig.class)
@DataMongoTest
@EnableAutoConfiguration
public class EmailChangeTokenVerificationServiceTest {

    @Autowired
    private TokenVerificationRepository verificationRepository;

    @Mock
    private IEmailService emailService;

    private IVerificationService<ChangeEmailVerificationToken> verificationService;

    @BeforeEach
    public void setUp() throws Exception {
        verificationService = new EmailChangeTokenVerificationService(verificationRepository, emailService);
    }

    @AfterEach
    public void tearDown() throws Exception {
        verificationRepository.deleteAll();
    }

    @Test
    public void createVerification() {
        var data = new EmailChangeVerificationData();
        var oleEmail = "admin@admin.com";
        var newEmail = "admin2@admin.com";

        data.setOldEmail(oleEmail);
        data.setNewEmail(newEmail);
        verificationService.createVerificationToken(data);

        var token = verificationService.getValidToken(getFirstToken());

        assertNotNull(token);
        assertEquals(oleEmail, token.getOldEmail());
        assertEquals(newEmail, token.getNewEmail());
    }

    private String getFirstToken() {
        var tokens = verificationRepository.findAll();
        if (tokens.isEmpty()) {
            return null;
        }
        return tokens.get(0).getToken();
    }
}