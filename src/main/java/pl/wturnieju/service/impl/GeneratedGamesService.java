package pl.wturnieju.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.GeneratedGameFixtureWrapper;
import pl.wturnieju.repository.GeneratedGamesRepository;
import pl.wturnieju.service.IGeneratedGamesService;

@RequiredArgsConstructor
@Service
public class GeneratedGamesService implements IGeneratedGamesService {

    private final GeneratedGamesRepository repository;

    @Override
    public void deleteAllByIds(List<String> gameFixturesIds) {
        gameFixturesIds.forEach(repository::deleteByGameFixture_Id);
    }

    @Override
    public void deleteById(String gameFixtureId) {
        repository.deleteByGameFixture_Id(gameFixtureId);
    }

    @Override
    public List<GameFixture> getAllByIds(List<String> gameFixtureIds) {
        List<GameFixture> games = new ArrayList<>();

        gameFixtureIds.forEach(id -> {
            var wrapper = repository.findByGameFixture_Id(id);
            if (wrapper != null) {
                games.add(wrapper.getGameFixture());
            }
        });

        return games;
    }

    @Override
    public List<GameFixture> insertAll(List<GameFixture> gameFixtures) {
        List<GeneratedGameFixtureWrapper> wrappers = new ArrayList<>();

        gameFixtures.forEach(game -> {
            game.setId(new ObjectId().toString());
            var wrapper = new GeneratedGameFixtureWrapper();
            wrapper.setGameFixture(game);
            wrappers.add(wrapper);
        });

        repository.insert(wrappers);
        return gameFixtures;
    }
}
