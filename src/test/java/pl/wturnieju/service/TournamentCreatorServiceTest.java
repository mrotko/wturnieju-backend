package pl.wturnieju.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.dto.ChessTournamentTemplateDto;
import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentFactory;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentSystemType;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.repository.UserRepository;

@SpringBootTest
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
    private ICurrentUser currentUser;

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
        setUpChessTournamentDto();
    }

    private void insertUser() {
        savedUser = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .build();
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
            Assert.assertEquals(tournament.getCompetitionType(), dto.getCompetitionType());
        });
    }

    @Test
    public void genericMappingTest() {
        Map<String, String> createdTournamentIdToClassNameMap = new HashMap<>();
        competitionTypeToTournamentDtoMap.values().forEach(dto -> {
            var className = TournamentFactory.getTournament(dto.getCompetitionType()).getClass().getName();
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

    private void setUpChessTournamentDto() {
        var dto = new ChessTournamentTemplateDto();

        dto.setMoveTimeSec(500);
        dto.setIncTimeSec(1);

        dto.setContributorsIds(Arrays.asList("1", "2", "3"));
        dto.setCompetitionType(CompetitionType.CHESS);
        dto.setDescription("chess competition description");
        dto.setStartDate(LocalDateTime.now().plusDays(10));
        dto.setEndDate(LocalDateTime.now());
        dto.setExpectedParticipants(5);
        dto.setMinParticipants(2);
        dto.setName("chess tournament");
        dto.setPlace("some place");
        dto.setStaffIds(Arrays.asList("1", "2", "3"));
        dto.setTournamentParticipantType(TournamentParticipantType.SINGLE);
        dto.setTournamentSystemType(TournamentSystemType.SWISS);

        competitionTypeToTournamentDtoMap.put(CompetitionType.CHESS, dto);
    }
}