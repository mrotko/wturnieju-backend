package pl.wturnieju.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import pl.wturnieju.model.AccessOption;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.TournamentStatus;

@Repository
public interface TournamentRepository extends MongoRepository<Tournament, String> {

    Tournament getById(String tournamentId);

    List<Tournament> getAllByStatus(TournamentStatus status);

    @Query("{accessOption: 'PUBLIC', name: {$regex: ?0, $options: 'i'}}")
    List<Tournament> findAllPublicByNameWithRegex(String regex, Pageable pageable);

    List<Tournament> getAllByAccessOption(AccessOption accessOption);
}
