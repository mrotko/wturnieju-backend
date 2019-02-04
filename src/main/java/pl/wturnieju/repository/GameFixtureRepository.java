package pl.wturnieju.repository;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.Timestamp;

@Repository
@Document(value = "test")
public interface GameFixtureRepository extends MongoRepository<GameFixture, String> {

    List<GameFixture> getAllByTournamentId(String tournamentId);

    List<GameFixture> getAllByGroupId(String groupId);

    List<GameFixture> getAllByGroupIdAndGameStatus(String groupId, GameStatus gameStatus);

    List<GameFixture> getAllByGroupIdAndGameStatusNotLike(String groupId, GameStatus gameStatus);

    Integer countAllByGroupIdAndGameStatusIsNotLike(String groupId, GameStatus gameStatus);

    List<GameFixture> getAllByAccessOptionAndStartDateBetween(AccessOption accessOption, Timestamp from, Timestamp to);
}
