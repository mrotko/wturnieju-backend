package pl.wturnieju.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.wturnieju.model.generic.TournamentState;

public interface TournamentStateRepository extends MongoRepository<TournamentState, String> {

    Optional<TournamentState> getByTournamentId(String tournamentId);
}
