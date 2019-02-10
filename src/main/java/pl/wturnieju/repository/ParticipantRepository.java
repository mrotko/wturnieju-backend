package pl.wturnieju.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mongodb.DBObject;

import pl.wturnieju.tournament.Participant;

public interface ParticipantRepository extends MongoRepository<Participant, String> {

    List<Participant> getAllByTournamentId(String tournamentId);

    @Query("?0")
    List<Participant> getAllByQuery(DBObject query);
}
