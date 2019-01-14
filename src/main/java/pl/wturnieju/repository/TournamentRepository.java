package pl.wturnieju.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.TournamentStatus;

@Repository
public interface TournamentRepository extends MongoRepository<Tournament, String> {

    Tournament getById(String tournamentId);

    List<Tournament> getAllByStatus(TournamentStatus status);

    //    List<Tournament> getAllByOwnerOrParticipants(IProfile owner, List<IProfile> participants);
}
