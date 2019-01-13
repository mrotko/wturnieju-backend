package pl.wturnieju.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.cli.dto.TournamentInfoResponse;
import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.inserter.TournamentInserter;
import pl.wturnieju.inserter.UserInserter;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.ITournamentCreatorService;
import pl.wturnieju.service.ITournamentParticipantService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.impl.TournamentCreatorService;
import pl.wturnieju.service.impl.TournamentParticipantService;
import pl.wturnieju.service.impl.TournamentService;
import pl.wturnieju.service.impl.UserService;
import pl.wturnieju.tournament.Tournament;

@Import(value = MongoConfig.class)
@ExtendWith(SpringExtension.class)
@DataMongoTest
@EnableAutoConfiguration
@WithMockCustomUser(username = "aukjan@yahoo.com")
public class TournamentCommandInterpreterTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ApplicationContext context;

    private IUserService userService;

    private ITournamentService tournamentService;

    private ITournamentCreatorService tournamentCreatorService;

    private ITournamentParticipantService tournamentParticipantService;

    private List<String> userTournamentsIds;

    private Tournament testedTournament;

    @BeforeEach
    public void setUp() throws Exception {
        setUpService();
        setUpInserter();
        testedTournament = tournamentRepository.findAll().get(0);
    }

    private void setUpService() {
        userService = new UserService(new BCryptPasswordEncoder(), userRepository);
        tournamentService = new TournamentService(tournamentRepository, context);
        tournamentCreatorService = new TournamentCreatorService(tournamentRepository, userService);
        tournamentParticipantService = new TournamentParticipantService(tournamentService, userService);
    }

    private void setUpInserter() {
        new UserInserter(userService, userRepository).insertUsersToDatabase();
        new TournamentInserter(tournamentCreatorService).insertTournamentToDatabase();
    }

    private void inviteUserToTournaments() {
        userTournamentsIds = tournamentRepository.findAll().stream()
                .limit(3)
                .map(Persistent::getId)
                .collect(Collectors.toList());

        //        userTournamentsIds.forEach(tournamentId -> tournamentParticipantService.invite(tournamentId, userService));
    }

    @Test
    public void getTournamentInfoTest() {
        var command1 = "tournament --id=" + testedTournament.getId();
        var command2 = "tournament -i=" + testedTournament.getId();

        var response1 = createInitializedInterpreterByCommand(command1).getResponse();
        var response2 = createInitializedInterpreterByCommand(command2).getResponse();

        var expectedResponse = new TournamentInfoResponse();
        expectedResponse.setTournamentId(testedTournament.getId());
        expectedResponse.setTournamentName(testedTournament.getName());
        expectedResponse.setStartDate(testedTournament.getStartDate());
        expectedResponse.setEndDate(testedTournament.getEndDate());
        expectedResponse.setStatus(testedTournament.getStatus().name());
        expectedResponse.setSystemName(testedTournament.getSystemType().name());
        expectedResponse.setCompetitionName(testedTournament.getCompetitionType().name());

        assertEquals(expectedResponse, response1);
        assertEquals(expectedResponse, response2);
    }

    @Test
    public void getTournamentEndedTest() {

    }

    @AfterEach
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        tournamentRepository.deleteAll();
    }


    private TournamentCommandInterpreter createInitializedInterpreterByCommand(String command) {
        var provider = createInitializedParsedDataProvider(command);
        return createInitializedTournamentCommandInterpreter(provider);
    }

    private ICommandParsedDataProvider createInitializedParsedDataProvider(String command) {
        try {
            var provider = new CliCommandParser(command);
            provider.parse();
            return provider;
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        return null;
    }

    private TournamentCommandInterpreter createInitializedTournamentCommandInterpreter(ICommandParsedDataProvider parsedDataProvider) {
        return (TournamentCommandInterpreter) CommandInterpreterFactory.createInterpreter(null, tournamentService, null,
                null, parsedDataProvider);
    }

}