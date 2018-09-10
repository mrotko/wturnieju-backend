package pl.wturnieju.service;

import java.util.List;

import pl.wturnieju.model.generic.Tournament;

public interface ITournamentService {
    List<Tournament> getAllActive();
}
