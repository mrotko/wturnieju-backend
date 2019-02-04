package pl.wturnieju.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.impl.UserService;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.Tournament;

@Import(value = MongoConfig.class)
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class TournamentParticipantServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    private ICurrentUserProvider currentUserProvider = Mockito.mock(UserService.class);

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private IUserService userService;

    private ITournamentService tournamentService;

    private ITournamentCreatorService tournamentCreatorService;

    private IParticipantService tournamentParticipantService;

    private User defaultUser;

    private Tournament defaultSingleTournament;

    @BeforeEach
    public void setUp() {
        //        userService = new UserService(passwordEncoder, userRepository);
        //        tournamentService = new TournamentService(tournamentRepository);
        //        tournamentParticipantService = new ParticipantService(tournamentService);
        //
        //        var userInserter = new UserInserter(userService, userRepository);
        //        userInserter.insertUsersToDatabase();
        //        defaultUser = userRepository.findByUsername("aukjan@yahoo.com").orElse(null);
        //
        //        Mockito.when(currentUserProvider.getCurrentUser()).thenReturn(defaultUser);
        //        tournamentCreatorService = new TournamentCreatorService(tournamentRepository, currentUserProvider);
        //
        //        var tournamentInserter = new TournamentInserter(tournamentCreatorService);
        //        tournamentInserter.insertTournamentToDatabase();
        //
        //        defaultSingleTournament = tournamentRepository.findAll().stream()
        //                .filter(tournament -> tournament.getParticipantType() == ParticipantType.SINGLE)
        //                .findFirst()
        //                .orElse(null);
    }

    @Test
    public void inviteNewParticipantShouldSuccess() {
        tournamentParticipantService.inviteUserId(defaultSingleTournament.getId(), defaultUser.getId());

        var participants = tournamentParticipantService.getAllByTournamentId(defaultSingleTournament.getId());

        Assertions.assertEquals(1, participants.size());

        var testedParticipant = participants.get(0);
        Assertions.assertEquals(defaultUser.getId(), testedParticipant.getId());
    }

    @Test
    public void invitedParticipantShouldHaveStatusInvited() {
        tournamentParticipantService.inviteUserId(defaultSingleTournament.getId(), defaultUser.getId());
        var testedParticipant = tournamentParticipantService.getAllByTournamentId(defaultSingleTournament.getId()).get(
                0);
        Assertions.assertEquals(ParticipantStatus.INVITED, testedParticipant.getParticipantStatus());
    }

    @Test
    public void inviteExistsParticipantShouldFail() {
        tournamentParticipantService.inviteUserId(defaultSingleTournament.getId(), defaultUser.getId());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> tournamentParticipantService.inviteUserId(defaultSingleTournament.getId(), defaultUser.getId()));
    }

    @Test
    public void inviteAsManyAsPossibleParticipantsShouldSuccess() {
        userRepository.findAll().stream()
                .limit(defaultSingleTournament.getRequirements().getMaxParticipants())
                .forEach(user -> tournamentParticipantService
                        .inviteUserId(defaultSingleTournament.getId(), user.getId()));
        Assertions.assertTrue(defaultSingleTournament.getRequirements().getMaxParticipants() > 0);
        Assertions.assertEquals(defaultSingleTournament.getRequirements().getMaxParticipants(),
                tournamentParticipantService.getAllByTournamentId(defaultSingleTournament.getId()).size());
    }


    @Test
    public void inviteParticipantWhenNoPlaceShouldFail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userRepository.findAll().stream()
                    .limit(defaultSingleTournament.getRequirements().getMaxParticipants() + 1)
                    .forEach(
                            user -> tournamentParticipantService
                                    .inviteUserId(defaultSingleTournament.getId(), user.getId()));
        });
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
        tournamentRepository.deleteAll();
    }
}