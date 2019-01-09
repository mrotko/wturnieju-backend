package pl.wturnieju.search;

import java.util.List;
import java.util.stream.Stream;

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
import pl.wturnieju.model.User;
import pl.wturnieju.repository.UserRepository;

@Import(MongoConfig.class)
@ExtendWith(SpringExtension.class)
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


    @BeforeEach
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

        Assertions.assertEquals(1, foundedUsers.size());
        Assertions.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findBySurnameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("spencer");

        Assertions.assertEquals(1, foundedUsers.size());
        Assertions.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByUsernameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("abAndreabSpencer123@test.com");

        Assertions.assertEquals(1, foundedUsers.size());
        Assertions.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void shouldIgnoreRedundantSpace() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("abAndreabSpencer123@test.com            andre");

        Assertions.assertEquals(1, foundedUsers.size());
        Assertions.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByNameAndSurnameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("andre spencer");

        Assertions.assertEquals(1, foundedUsers.size());
        Assertions.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByNameAndUsernameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("andre abAndreabSpencer123@test.com");

        Assertions.assertEquals(1, foundedUsers.size());
        Assertions.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByNameAndSurnameAndUsernameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("spencer abAndreabSpencer123@test.com");

        Assertions.assertEquals(1, foundedUsers.size());
        Assertions.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findByPartialNameAndPartialSurnameShouldFindSingle() {
        User expectedUser = userRepository.findByUsername("abAndreabSpencer123@test.com")
                .orElseThrow(AssertionError::new);
        List<User> foundedUsers = search.find("an spe");

        Assertions.assertEquals(1, foundedUsers.size());
        Assertions.assertEquals(expectedUser, foundedUsers.get(0));
    }

    @Test
    public void findShouldNotFindAnyone() {
        List<User> foundedUsers = search.find("dummy");
        Assertions.assertEquals(0, foundedUsers.size());
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }
}