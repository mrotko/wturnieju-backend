package pl.wturnieju.service;

import java.util.List;
import java.util.Optional;

import pl.wturnieju.model.generic.Tournament;

public interface ITournamentService {

    void update(Tournament tournament);

    List<Tournament> getAllActive();

    Optional<Tournament> getById(String tournamentId);
}
