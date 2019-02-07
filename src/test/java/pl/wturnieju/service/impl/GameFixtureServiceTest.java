package pl.wturnieju.service.impl;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.repository.GameFixtureRepository;
import pl.wturnieju.tournament.Participant;

@Import(value = MongoConfig.class)
@ExtendWith(SpringExtension.class)
@DataMongoTest
class GameFixtureServiceTest {

    @Autowired
    private GameFixtureRepository gameFixtureRepository;

    private GameFixtureService gameFixtureService;

    @BeforeEach
    void setUp() {
        gameFixtureService = new GameFixtureService(gameFixtureRepository);
    }

    @AfterEach
    void tearDown() {
        gameFixtureRepository.deleteAll();
    }

    @Test
    void shouldReturnAllParticipantsGames() {
        String firstParticipantId = new ObjectId().toString();
        String secondParticipantId = new ObjectId().toString();

        GameFixture gameFixture = new GameFixture();
        gameFixture.setId(new ObjectId().toString());
        gameFixture.setHomeParticipant(new Participant());
        gameFixture.getHomeParticipant().setId(firstParticipantId);

        gameFixture.setAwayParticipant(new Participant());
        gameFixture.getAwayParticipant().setId(secondParticipantId);

        gameFixtureRepository.insert(gameFixture);

        Assertions.assertEquals(
                gameFixture.getId(),
                gameFixtureService.findLastParticipantsGame(firstParticipantId, secondParticipantId)
                        .map(Persistent::getId).orElse(null)
        );
        Assertions.assertEquals(
                gameFixture.getId(),
                gameFixtureService.findLastParticipantsGame(secondParticipantId, firstParticipantId)
                        .map(Persistent::getId).orElse(null)
        );
    }
}