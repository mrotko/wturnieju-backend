package pl.wturnieju.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.generator.CurrentUserGenerator;
import pl.wturnieju.generator.TournamentCreatorDtoGenerator;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.User;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.EndTournamentBundleUpdateContent;
import pl.wturnieju.service.ICurrentUserProvider;
import pl.wturnieju.service.ITournamentCreatorService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.StartTournamentBundleUpdateContent;
import pl.wturnieju.service.SwissTournamentUpdateBundle;
import pl.wturnieju.service.TournamentCreatorService;
import pl.wturnieju.service.TournamentService;

@Import(MongoConfig.class)
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class SwissTournamentSystemTest {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UserRepository userRepository;

    private ITournamentCreatorService tournamentCreatorService;

    private ITournamentService tournamentService;

    @Mock
    private ICurrentUserProvider currentUserProvider;


    private User savedUser;

    private String savedTournamentId;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        insertUser();
        Mockito.when(currentUserProvider.getCurrentUser()).thenReturn(savedUser);

        tournamentCreatorService = new TournamentCreatorService(tournamentRepository, currentUserProvider);
        insertTournament();

        tournamentService = new TournamentService(tournamentRepository);
    }

    @Test
    public void testStartTournament() {

        SwissTournamentUpdateBundle bundle = new SwissTournamentUpdateBundle();
        bundle.setChangedBy(currentUserProvider.getCurrentUser());
        bundle.setTimestamp(Timestamp.now());
        bundle.setTournamentId(savedTournamentId);

        Timestamp startDate = new Timestamp(LocalDateTime.now().plusDays(1));
        StartTournamentBundleUpdateContent content = new StartTournamentBundleUpdateContent();
        content.setStartDate(startDate);

        bundle.setContent(content);

        tournamentService.updateTournament(bundle);

        Tournament tournament = tournamentService.getById(savedTournamentId).orElseThrow();

        assertEquals(TournamentStatus.IN_PROGRESS, tournament.getStatus());
        assertEquals(startDate, tournament.getStartDate());
    }

    @Test
    public void unauthorizedAccessTest() {
        // TODO(mr): 15.11.2018 unauthorizedAccessTest (brakuje całego serwisu autoryzacji)
    }

    @Test
    public void testEndTournament() {
        SwissTournamentUpdateBundle bundle = new SwissTournamentUpdateBundle();
        bundle.setChangedBy(currentUserProvider.getCurrentUser());
        bundle.setTimestamp(Timestamp.now());
        bundle.setTournamentId(savedTournamentId);

        Timestamp stopDate = new Timestamp(LocalDateTime.now().plusDays(1));
        EndTournamentBundleUpdateContent content = new EndTournamentBundleUpdateContent();
        content.setEndDate(stopDate);

        bundle.setContent(content);

        tournamentService.updateTournament(bundle);

        Tournament tournament = tournamentService.getById(savedTournamentId).orElseThrow();

        assertEquals(TournamentStatus.ENDED, tournament.getStatus());
        assertEquals(stopDate, tournament.getEndDate());
    }

    private void insertTournament() {
        savedTournamentId = tournamentCreatorService.create(
                TournamentCreatorDtoGenerator.generateChessTournaments().get(0)).getId();
    }

    private void insertUser() {
        savedUser = CurrentUserGenerator.generateUser();
        savedUser.setPassword(encoder.encode(savedUser.getPassword()));
        userRepository.save(savedUser);
    }


    @AfterEach
    public void tearDown() throws Exception {
        tournamentRepository.deleteAll();
        userRepository.deleteAll();
    }

}