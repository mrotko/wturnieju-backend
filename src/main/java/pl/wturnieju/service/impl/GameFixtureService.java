package pl.wturnieju.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.repository.GameFixtureRepository;
import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.tournament.LegType;

@RequiredArgsConstructor
@Service
public class GameFixtureService implements IGameFixtureService {

    private final GameFixtureRepository repository;

    @Override
    public GameFixture update(GameFixture gameFixture) {
        return repository.save(gameFixture);
    }

    @Override
    public List<GameFixture> updateAll(List<GameFixture> gameFixtures) {
        return repository.saveAll(gameFixtures);
    }

    @Override
    public GameFixture save(GameFixture gameFixture) {
        return repository.insert(gameFixture);
    }

    @Override
    public List<GameFixture> saveAll(List<GameFixture> gameFixtures) {
        return repository.saveAll(gameFixtures);
    }

    @Override
    public void deleteAllByIds(List<String> gameFixturesIds) {
        gameFixturesIds.forEach(repository::deleteById);
    }

    @Override
    public void deleteById(String gameFixtureId) {
        repository.deleteById(gameFixtureId);
    }

    @Override
    public List<GameFixture> getAllByGroupId(String groupId) {
        return repository.getAllByGroupId(groupId);
    }

    @Override
    public List<GameFixture> getAllByGroupIdAndLegType(String groupId, LegType legType) {
        return repository.getAllByGroupIdAndLegType(groupId, legType);
    }

    @Override
    public List<GameFixture> getAllPendingGamesByGroupId(String groupId) {
        return repository.getAllByGroupIdAndGameStatusNotLike(groupId, GameStatus.ENDED);
    }

    @Override
    public Integer countPendingGamesByGroupId(String groupId) {
        return repository.countAllByGroupIdAndGameStatusIsNotLike(groupId, GameStatus.ENDED);
    }

    @Override
    public List<GameFixture> getAllEndedByGroupId(String groupId) {
        return repository.getAllByGroupIdAndGameStatus(groupId, GameStatus.ENDED);
    }

    @Override
    public GameFixture getById(String gameFixtureId) {
        return repository.findById(gameFixtureId).orElse(null);
    }

    @Override
    public List<GameFixture> getAllByTournamentId(String tournamentId) {
        return repository.getAllByTournamentId(tournamentId);
    }

    @Override
    public List<GameFixture> getAllPublicStartsBetweenDates(Timestamp dateFrom, Timestamp dateTo) {
        return repository.getAllByAccessOptionAndStartDateBetween(AccessOption.PUBLIC, dateFrom, dateTo);
    }

    @Override
    public Optional<GameFixture> findLastParticipantsGame(String firstParticipantId, String secondParticipantId) {
        DBObject query = QueryBuilder.start().or(
                QueryBuilder.start().and(
                        QueryBuilder.start().put("homeParticipant.id").is(firstParticipantId).get(),
                        QueryBuilder.start().put("awayParticipant.id").is(secondParticipantId).get()).get(),
                QueryBuilder.start().and(
                        QueryBuilder.start().put("homeParticipant.id").is(secondParticipantId).get(),
                        QueryBuilder.start().put("awayParticipant.id").is(firstParticipantId).get()).get()).get();

        return repository.findAllByQuery(query).stream()
                .min(Comparator.comparing(GameFixture::getStartDate));
    }
}
