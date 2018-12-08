package pl.wturnieju.service;

import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.inserter.TournamentInserter;
import pl.wturnieju.inserter.UserInserter;
import pl.wturnieju.model.ParticipantStatus;
import pl.wturnieju.model.TournamentParticipant;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.User;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.repository.UserRepository;

//@SpringBootTest
@Import(value = MongoConfig.class)
@RunWith(SpringRunner.class)
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

    private ITournamentParticipantService tournamentParticipantService;

    private User defaultUser;

    private Tournament defaultSingleTournament;

    @Before
    public void setUp() {
        userService = new UserService(passwordEncoder, userRepository);
        tournamentService = new TournamentService(tournamentRepository);
        tournamentParticipantService = new TournamentParticipantService(tournamentService);

        var userInserter = new UserInserter(userService, userRepository);
        userInserter.insertUsersToDatabase();
        defaultUser = userRepository.findByUsername("aukjan@yahoo.com").orElse(null);

        Mockito.when(currentUserProvider.getCurrentUser()).thenReturn(defaultUser);
        tournamentCreatorService = new TournamentCreatorService(tournamentRepository, currentUserProvider);

        var tournamentInserter = new TournamentInserter(tournamentCreatorService);
        tournamentInserter.insertTournamentToDatabase();

        defaultSingleTournament = tournamentRepository.findAll().stream()
                .filter(tournament -> tournament.getTournamentParticipantType() == TournamentParticipantType.SINGLE)
                .findFirst()
                .orElse(null);
    }

    @Test
    public void inviteNewParticipantShouldSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());

        var participants = tournamentParticipantService.getAll(defaultSingleTournament.getId());

        Assert.assertEquals(1, participants.size());

        var testedParticipant = participants.get(0);
        Assert.assertEquals(defaultUser.getId(), testedParticipant.getId());
    }

    @Test
    public void invitedParticipantShouldHaveStatusInvited() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        var testedParticipant = tournamentParticipantService.getAll(defaultSingleTournament.getId()).get(0);
        Assert.assertEquals(ParticipantStatus.INVITED, testedParticipant.getParticipantStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void inviteExistsParticipantShouldFail() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
    }

    @Test
    public void inviteAsManyAsPossibleParticipantsShouldSuccess() {
        userRepository.findAll().stream()
                .limit(defaultSingleTournament.getMaxParticipants())
                .forEach(user -> tournamentParticipantService.invite(defaultSingleTournament.getId(), user.getId()));
        Assert.assertTrue(defaultSingleTournament.getMaxParticipants() > 0);
        Assert.assertEquals(defaultSingleTournament.getMaxParticipants(),
                tournamentParticipantService.getAll(defaultSingleTournament.getId()).size());
    }


    @Test(expected = IllegalArgumentException.class)
    public void inviteParticipantWhenNoPlaceShouldFail() {
        userRepository.findAll().stream()
                .limit(defaultSingleTournament.getMaxParticipants() + 1)
                .forEach(user -> tournamentParticipantService.invite(defaultSingleTournament.getId(), user.getId()));
    }

    @Test
    public void getByIdShouldReturnParticipantSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());

        Assert.assertEquals(defaultUser.getId(),
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId()).map(
                        TournamentParticipant::getId).orElse(null));
    }

    @Test
    public void getByIdShouldReturnEmptyBecauseBadTournamentId() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());

        Assert.assertEquals(Optional.empty(),
                tournamentParticipantService.getById(defaultSingleTournament.getId() + 1, defaultUser.getId()));
    }

    @Test
    public void confirmParticipationShouldSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());

        Assert.assertEquals(ParticipantStatus.ACTIVE,
                tournamentParticipantService.getById(defaultSingleTournament.getId(),
                        defaultUser.getId())
                        .map(TournamentParticipant::getParticipantStatus)
                        .orElse(null));
    }

    @Test
    public void getByIdShouldReturnEmptyBecauseEmptyParticipantList() {
        Assert.assertEquals(Optional.empty(),
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId()));
    }

    @Test
    public void getByIdShouldReturnEmptyBecauseBadParticipantId() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());

        Assert.assertEquals(Optional.empty(),
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId() + 1));
    }

    @Test
    public void doResignAfterConfirmationSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.doResign(defaultSingleTournament.getId(), defaultUser.getId());

        Assert.assertEquals(ParticipantStatus.RESIGNED,
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                        .map(TournamentParticipant::getParticipantStatus)
                        .orElse(null));
    }

    @Test
    public void doResignAfterInvitationShouldRemoveParticipantSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.doResign(defaultSingleTournament.getId(), defaultUser.getId());

        Assert.assertNull(tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                .orElse(null));
    }

    @Test
    public void doResignShouldDoNothingBecauseIncorrectIds() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());

        tournamentParticipantService.doResign(defaultSingleTournament.getId() + 1, defaultUser.getId());
        tournamentParticipantService.doResign(defaultSingleTournament.getId(), defaultUser.getId() + 1);

        Assert.assertEquals(ParticipantStatus.ACTIVE,
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                        .map(TournamentParticipant::getParticipantStatus)
                        .orElse(null));
    }

    @Test
    public void doDisqualifySuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());

        tournamentParticipantService.doDisqualify(defaultSingleTournament.getId(), defaultUser.getId(), "");

        Assert.assertEquals(ParticipantStatus.DISQUALIFIED,
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                        .map(TournamentParticipant::getParticipantStatus)
                        .orElse(null));
    }

    @Test
    public void doDisqualifyShouldDoNothingBecauseIncorrectIds() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());

        tournamentParticipantService.doDisqualify(defaultSingleTournament.getId() + 1, defaultUser.getId(), "");
        tournamentParticipantService.doDisqualify(defaultSingleTournament.getId(), defaultUser.getId() + 1, "");

        Assert.assertEquals(ParticipantStatus.ACTIVE,
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                        .map(TournamentParticipant::getParticipantStatus)
                        .orElse(null));
    }

    @After
    public void clean() {
        userRepository.deleteAll();
        tournamentRepository.deleteAll();
    }
}