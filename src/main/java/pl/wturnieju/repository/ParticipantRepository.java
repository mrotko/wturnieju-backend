package pl.wturnieju.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.wturnieju.tournament.Participant;

public interface ParticipantRepository extends MongoRepository<Participant, String> {

    List<Participant> getAllByTournamentId(String tournamentId);
}
