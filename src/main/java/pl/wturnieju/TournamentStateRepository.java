package pl.wturnieju;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TournamentStateRepository extends MongoRepository<TournamentState, String> {

    Optional<TournamentState> getByTournamentId(String tournamentId);
}
