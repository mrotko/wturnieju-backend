package pl.wturnieju.repository;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.wturnieju.model.GeneratedGameFixtureWrapper;

@Repository
@Document(collection = "test2")
public interface GeneratedGamesRepository extends MongoRepository<GeneratedGameFixtureWrapper, String> {

    GeneratedGameFixtureWrapper getByGameFixture_Id(String gameFixtureId);

    void deleteByGameFixture_Id(String gameFixtureId);

    GeneratedGameFixtureWrapper findByGameFixture_Id(String gameFixtureId);
}


