package pl.wturnieju.search;

import java.util.List;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.UserRepository;

@Import(MongoConfig.class)
@RunWith(SpringRunner.class)
@DataMongoTest
public class UserSimpleSearchServiceTest {

    @Autowired
    private UserRepository userRepository;

    private ISearch<String, User> search;

    private User createUser(String name, String surname, String username) {
        return User.builder()
                .username(username)
                .name(name)
                .surname(surname)
                .build();
    }


    @Before
    public void setUp() {
        search = new UserSimpleSearchService(userRepository);
        Stream.of(
                createUser("Shaun", "Jimenez", "abShaunabJimenez123@test.com"),
                createUser("Rene", "Reyes", "abReneabReyes123@test.com"),
                createUser("Katherine", "Boone", "abKatherineabBoone123@test.com"),
                createUser("Alonzo", "Park", "abAlonzoabPark123@test.com"),
                createUser("Sally", "Lynch", "abSallyabLynch123@test.com"),
                createUser("Tom", "Hunt", "abTomabHunt123@test.com"),
                createUser("Frederick", "Higgins", "abFrederickabHiggins123@test.com"),
                createUser("Andre", "Spencer", "abAndreabSpencer123@test.com"),
                createUser("Marta", "Mann", "abMartaabMann123@test.com"),
                createUser("Bobbie", "Burns", "abBobbieabBurns123@test.com")
        ).forEach(user -> userRepository.insert(user));
    }

    @Test
    public void findByNameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("andre");

        Assert.assertEquals(1, foundedUsers.size());
        Assert.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findBySurnameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("spencer");

        Assert.assertEquals(1, foundedUsers.size());
        Assert.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByUsernameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("abAndreabSpencer123@test.com");

        Assert.assertEquals(1, foundedUsers.size());
        Assert.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void shouldIgnoreRedundantSpace() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("abAndreabSpencer123@test.com            andre");

        Assert.assertEquals(1, foundedUsers.size());
        Assert.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByNameAndSurnameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("andre spencer");

        Assert.assertEquals(1, foundedUsers.size());
        Assert.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByNameAndUsernameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("andre abAndreabSpencer123@test.com");

        Assert.assertEquals(1, foundedUsers.size());
        Assert.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByNameAndSurnameAndUsernameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("spencer abAndreabSpencer123@test.com");

        Assert.assertEquals(1, foundedUsers.size());
        Assert.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByPartialNameAndPartialSurnameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("an spe");

        Assert.assertEquals(1, foundedUsers.size());
        Assert.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findShouldNotFindAnyone() {
        List<User> foundedUsers = search.find("dummy");
        Assert.assertEquals(0, foundedUsers.size());
    }

    @After
    public void clean() {
        userRepository.deleteAll();
    }
}