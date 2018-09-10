package pl.wturnieju.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.model.User;
import pl.wturnieju.repository.UserRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@DataMongoTest
@ActiveProfiles(profiles = "test")
public class TournamentCreatorServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void insertTest() {
        User user = new User();
        user.setUsername("testowy username");

        System.out.println(user.getUsername());
        userRepository.save(user);
        System.out.println(user.getId());

        User founded = userRepository.findAll().stream().findFirst().orElse(null);

        System.out.println(founded);
        Assert.assertEquals(user.getId(), founded.getId());
    }
}