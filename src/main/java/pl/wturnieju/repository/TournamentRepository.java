package pl.wturnieju.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.generic.Tournament;

@Repository
public interface TournamentRepository extends MongoRepository<Tournament, String> {

    List<Tournament> getAllByStatus(TournamentStatus status);
}
