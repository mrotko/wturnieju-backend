package pl.wturnieju.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.wturnieju.tournament.system.state.Group;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

    List<Group> getAllByTournamentId(String tournamentId);

}
