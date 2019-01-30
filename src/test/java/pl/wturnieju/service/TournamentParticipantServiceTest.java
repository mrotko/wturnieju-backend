package pl.wturnieju.service;

import java.util.Optional;

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
import pl.wturnieju.tournament.Participant;
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

    private ITournamentParticipantService tournamentParticipantService;

    private User defaultUser;

    private Tournament defaultSingleTournament;

    @BeforeEach
    public void setUp() {
        //        userService = new UserService(passwordEncoder, userRepository);
        //        tournamentService = new TournamentService(tournamentRepository);
        //        tournamentParticipantService = new TournamentParticipantService(tournamentService);
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
        //                .filter(tournament -> tournament.getTournamentParticipantType() == TournamentParticipantType.SINGLE)
        //                .findFirst()
        //                .orElse(null);
    }

    @Test
    public void inviteNewParticipantShouldSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());

        var participants = tournamentParticipantService.getAll(defaultSingleTournament.getId());

        Assertions.assertEquals(1, participants.size());

        var testedParticipant = participants.get(0);
        Assertions.assertEquals(defaultUser.getId(), testedParticipant.getId());
    }

    @Test
    public void invitedParticipantShouldHaveStatusInvited() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        var testedParticipant = tournamentParticipantService.getAll(defaultSingleTournament.getId()).get(0);
        Assertions.assertEquals(ParticipantStatus.INVITED, testedParticipant.getParticipantStatus());
    }

    @Test
    public void inviteExistsParticipantShouldFail() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId()));
    }

    @Test
    public void inviteAsManyAsPossibleParticipantsShouldSuccess() {
        userRepository.findAll().stream()
                .limit(defaultSingleTournament.getRequirements().getMaxParticipants())
                .forEach(user -> tournamentParticipantService.invite(defaultSingleTournament.getId(), user.getId()));
        Assertions.assertTrue(defaultSingleTournament.getRequirements().getMaxParticipants() > 0);
        Assertions.assertEquals(defaultSingleTournament.getRequirements().getMaxParticipants(),
                tournamentParticipantService.getAll(defaultSingleTournament.getId()).size());
    }


    @Test
    public void inviteParticipantWhenNoPlaceShouldFail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userRepository.findAll().stream()
                    .limit(defaultSingleTournament.getRequirements().getMaxParticipants() + 1)
                    .forEach(
                            user -> tournamentParticipantService.invite(defaultSingleTournament.getId(), user.getId()));
        });
    }

    @Test
    public void getByIdShouldReturnParticipantSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());

        Assertions.assertEquals(defaultUser.getId(),
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId()).map(
                        Participant::getId).orElse(null));
    }

    @Test
    public void getByIdShouldReturnEmptyBecauseBadTournamentId() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());

        Assertions.assertEquals(Optional.empty(),
                tournamentParticipantService.getById(defaultSingleTournament.getId() + 1, defaultUser.getId()));
    }

    @Test
    public void confirmParticipationShouldSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        //        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());

        Assertions.assertEquals(ParticipantStatus.ACTIVE,
                tournamentParticipantService.getById(defaultSingleTournament.getId(),
                        defaultUser.getId())
                        .map(Participant::getParticipantStatus)
                        .orElse(null));
    }

    @Test
    public void getByIdShouldReturnEmptyBecauseEmptyParticipantList() {
        Assertions.assertEquals(Optional.empty(),
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId()));
    }

    @Test
    public void getByIdShouldReturnEmptyBecauseBadParticipantId() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());

        Assertions.assertEquals(Optional.empty(),
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId() + 1));
    }

    @Test
    public void doResignAfterConfirmationSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        //        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.doResign(defaultSingleTournament.getId(), defaultUser.getId());

        Assertions.assertEquals(ParticipantStatus.RESIGNED,
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                        .map(Participant::getParticipantStatus)
                        .orElse(null));
    }

    @Test
    public void doResignAfterInvitationShouldRemoveParticipantSuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        tournamentParticipantService.doResign(defaultSingleTournament.getId(), defaultUser.getId());

        Assertions.assertNull(tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                .orElse(null));
    }

    @Test
    public void doResignShouldDoNothingBecauseIncorrectIds() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        //        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());

        tournamentParticipantService.doResign(defaultSingleTournament.getId() + 1, defaultUser.getId());
        tournamentParticipantService.doResign(defaultSingleTournament.getId(), defaultUser.getId() + 1);

        Assertions.assertEquals(ParticipantStatus.ACTIVE,
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                        .map(Participant::getParticipantStatus)
                        .orElse(null));
    }

    @Test
    public void doDisqualifySuccess() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        //        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());

        tournamentParticipantService.doDisqualify(defaultSingleTournament.getId(), defaultUser.getId(), "");

        Assertions.assertEquals(ParticipantStatus.DISQUALIFIED,
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                        .map(Participant::getParticipantStatus)
                        .orElse(null));
    }

    @Test
    public void doDisqualifyShouldDoNothingBecauseIncorrectIds() {
        tournamentParticipantService.invite(defaultSingleTournament.getId(), defaultUser.getId());
        //        tournamentParticipantService.confirm(defaultSingleTournament.getId(), defaultUser.getId());

        tournamentParticipantService.doDisqualify(defaultSingleTournament.getId() + 1, defaultUser.getId(), "");
        tournamentParticipantService.doDisqualify(defaultSingleTournament.getId(), defaultUser.getId() + 1, "");

        Assertions.assertEquals(ParticipantStatus.ACTIVE,
                tournamentParticipantService.getById(defaultSingleTournament.getId(), defaultUser.getId())
                        .map(Participant::getParticipantStatus)
                        .orElse(null));
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
        tournamentRepository.deleteAll();
    }
}