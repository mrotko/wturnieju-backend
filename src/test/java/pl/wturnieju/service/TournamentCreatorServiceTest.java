package pl.wturnieju.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import pl.wturnieju.configuration.WithMockCustomUser;
import pl.wturnieju.controller.dto.tournament.creator.TournamentTemplateDto;
import pl.wturnieju.generator.CurrentUserGenerator;
import pl.wturnieju.generator.TournamentCreatorDtoGenerator;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentFactory;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.impl.TournamentCreatorService;

@Import(value = MongoConfig.class)
@ExtendWith(SpringExtension.class)
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

    @BeforeEach
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
        Assertions.assertEquals(competitionTypeToTournamentDtoMap.size(), CompetitionType.values().length);
    }

    @Test
    public void shouldSaveTournaments() {
        Map<String, TournamentTemplateDto> createdTournamentIdToDto = new HashMap<>();
        competitionTypeToTournamentDtoMap.values().forEach(dto -> {
            //            var tournament = tournamentCreatorService.create(dto);
            //            createdTournamentIdToDto.put(tournament.getId(), dto);
        });

        Assertions.assertEquals(createdTournamentIdToDto.size(), competitionTypeToTournamentDtoMap.size());

        createdTournamentIdToDto.forEach((id, dto) -> {
            var tournament = tournamentRepository.findById(id).orElse(null);
            Assertions.assertNotNull(tournament);
            Assertions.assertEquals(tournament.getCompetitionType(), dto.getCompetitionType());
        });
    }

    @Test
    public void genericMappingTest() {
        Map<String, String> createdTournamentIdToClassNameMap = new HashMap<>();
        competitionTypeToTournamentDtoMap.values().forEach(dto -> {
            var className = TournamentFactory.getTournament(dto.getCompetitionType()).getClass().getName();
            //            var tournament = tournamentCreatorService.create(dto);
            //            createdTournamentIdToClassNameMap.put(tournament.getId(), className);
        });

        Assertions.assertEquals(createdTournamentIdToClassNameMap.size(), competitionTypeToTournamentDtoMap.size());

        createdTournamentIdToClassNameMap.forEach((id, className) -> {
            var tournament = tournamentRepository.findById(id).orElse(null);
            Assertions.assertNotNull(tournament);
            Assertions.assertEquals(tournament.getClass().getName(), className);
        });
    }

    @AfterEach
    public void tearDown() throws Exception {
        tournamentRepository.deleteAll();
        userRepository.deleteAll();
    }
}