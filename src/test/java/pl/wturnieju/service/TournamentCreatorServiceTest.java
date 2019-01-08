package pl.wturnieju.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.config.MongoConfig;
import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.generator.CurrentUserGenerator;
import pl.wturnieju.generator.TournamentCreatorDtoGenerator;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentFactory;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.repository.UserRepository;

@Import(value = MongoConfig.class)
@RunWith(SpringRunner.class)
@DataMongoTest
@WithMockCustomUser
public class TournamentCreatorServiceTest {

    private final String username = "email@email.com";

    private final String password = "Password123,";

    private Map<CompetitionType, TournamentTemplateDto> competitionTypeToTournamentDtoMap = new HashMap<>();

    @Autowired
    private TournamentRepository tournamentRepository;

    @Mock
    private ICurrentUserProvider currentUser;

    private TournamentCreatorService tournamentCreatorService;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Before
    public void setUp() {
        userRepository.deleteAll();
        insertUser();

        Mockito.when(currentUser.getCurrentUser()).thenReturn(savedUser);
        tournamentCreatorService = new TournamentCreatorService(tournamentRepository, currentUser);
        competitionTypeToTournamentDtoMap.put(CompetitionType.CHESS,
                TournamentCreatorDtoGenerator.generateChessTournaments().get(0));
    }

    private void insertUser() {
        savedUser = CurrentUserGenerator.generateUser();
        savedUser.setPassword(encoder.encode(savedUser.getPassword()));
        userRepository.insert(savedUser);
    }

    @Test
    public void allCompetitionsIncludedTest() {
        Assert.assertEquals(competitionTypeToTournamentDtoMap.size(), CompetitionType.values().length);
    }

    @Test
    public void shouldSaveTournaments() {
        Map<String, TournamentTemplateDto> createdTournamentIdToDto = new HashMap<>();
        competitionTypeToTournamentDtoMap.values().forEach(dto -> {
            var tournament = tournamentCreatorService.create(dto);
            createdTournamentIdToDto.put(tournament.getId(), dto);
        });

        Assert.assertEquals(createdTournamentIdToDto.size(), competitionTypeToTournamentDtoMap.size());

        createdTournamentIdToDto.forEach((id, dto) -> {
            var tournament = tournamentRepository.findById(id).orElse(null);
            Assert.assertNotNull(tournament);
            Assert.assertEquals(tournament.getCompetitionType(), dto.getCompetition());
        });
    }

    @Test
    public void genericMappingTest() {
        Map<String, String> createdTournamentIdToClassNameMap = new HashMap<>();
        competitionTypeToTournamentDtoMap.values().forEach(dto -> {
            var className = TournamentFactory.getTournament(dto.getCompetition()).getClass().getName();
            var tournament = tournamentCreatorService.create(dto);
            createdTournamentIdToClassNameMap.put(tournament.getId(), className);
        });

        Assert.assertEquals(createdTournamentIdToClassNameMap.size(), competitionTypeToTournamentDtoMap.size());

        createdTournamentIdToClassNameMap.forEach((id, className) -> {
            var tournament = tournamentRepository.findById(id).orElse(null);
            Assert.assertNotNull(tournament);
            Assert.assertEquals(tournament.getClass().getName(), className);
        });
    }

    @After
    public void tearDown() throws Exception {
        tournamentRepository.deleteAll();
        userRepository.deleteAll();
    }
}