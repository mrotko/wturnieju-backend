package pl.wturnieju.service;

import java.util.List;

import pl.wturnieju.dto.FixtureDTO;
import pl.wturnieju.model.generic.GenericTournamentTable;

public interface ITournamentStandingService {

    GenericTournamentTable getTable(String tournamentId);

    List<FixtureDTO> getFixtures(String tournamentId);

    List<FixtureDTO> getFixtures(String tournamentId, String playerId);
}
