package pl.wturnieju.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.wturnieju.tournament.system.table.TournamentTable;

@Repository
public interface TournamentTableRepository extends MongoRepository<TournamentTable, String> {
}
