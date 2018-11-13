package pl.wturnieju.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.FixtureDTO;
import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.generic.GenericTournamentTable;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentSystemState;
import pl.wturnieju.repository.TournamentRepository;

@Service
@RequiredArgsConstructor
public class TournamentStandingService implements ITournamentStandingService {

    private final TournamentRepository tournamentRepository;

    @Override
    public GenericTournamentTable getTable(String tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(Tournament::getTournamentSystemState)
                .map(TournamentSystemState::getTournamentTable)
                .orElse(null);
    }

    @Override
    public List<FixtureDTO> getFixtures(String tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(Tournament::getTournamentSystemState)
                .map(TournamentSystemState::getFixtures)
                .map(this::createFixtures)
                .map(fixtures -> {
                    fixtures.sort(Comparator.comparing(FixtureDTO::getTimestamp).reversed());
                    return fixtures;
                }).orElse(Collections.emptyList());
    }

    @Override
    public List<FixtureDTO> getFixtures(String tournamentId, String playerId) {
        return getFixtures(tournamentId).stream()
                .filter(dto -> dto.getPlayers().getLeft().getId().equals(playerId)
                        || dto.getPlayers().getRight().getId().equals(playerId))
                .collect(Collectors.toList());
    }

    private List<FixtureDTO> createFixtures(Collection<Fixture> fixtures) {
        return fixtures.stream()
                .map(fixture -> {
                    var dto = new FixtureDTO();

                    dto.setPlayers(fixture.getPlayers());
                    dto.setPoints(fixture.getPoints());
                    dto.setStatus(fixture.getStatus());
                    dto.setTimestamp(fixture.getTimestamp());

                    return dto;
                })
                .collect(Collectors.toList());
    }
}
