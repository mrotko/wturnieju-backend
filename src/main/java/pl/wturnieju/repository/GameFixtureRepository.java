package pl.wturnieju.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.wturnieju.gamefixture.GameFixture;

@Repository
public interface GameFixtureRepository extends MongoRepository<GameFixture, String> {

}
