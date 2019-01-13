package pl.wturnieju.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.wturnieju.tournament.system.state.SystemState;

public interface SystemStateRepository<T extends SystemState> extends MongoRepository<T, String> {

    T getByTournamentId(String tournamentId);
}
