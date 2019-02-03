package pl.wturnieju.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.wturnieju.tournament.system.state.SystemState;

public interface SystemStateRepository extends MongoRepository<SystemState, String> {

    SystemState getByTournamentId(String tournamentId);
}
