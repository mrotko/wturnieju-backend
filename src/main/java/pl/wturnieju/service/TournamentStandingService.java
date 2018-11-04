package pl.wturnieju.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.FixtureDTO;
import pl.wturnieju.model.Duel;
import pl.wturnieju.model.generic.GenericTournamentTable;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentState;
import pl.wturnieju.repository.TournamentRepository;

@Service
@RequiredArgsConstructor
public class TournamentStandingService implements ITournamentStandingService {

    private final TournamentRepository tournamentRepository;

    @Override
    public GenericTournamentTable getTable(String tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(Tournament::getTournamentState)
                .map(TournamentState::getTournamentTable)
                .orElse(null);
    }

    @Override
    public List<FixtureDTO> getFixtures(String tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(Tournament::getTournamentState)
                .map(TournamentState::getRoundToDuelsMap)
                .map(roundToDuels -> roundToDuels.values().stream().flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .map(this::createFixtures)
                .map(fixtures -> {
                    fixtures.sort(Comparator.comparing(FixtureDTO::getTimestamp).reversed());
                    return fixtures;
                }).orElse(Collections.emptyList());
    }

    @Override
    public List<FixtureDTO> getFixtures(String tournamentId, String playerId) {
        return getFixtures(tournamentId).stream()
                .filter(dto -> dto.getHomePlayer().getId().equals(playerId)
                        || dto.getAwayPlayer().getId().equals(playerId))
                .collect(Collectors.toList());
    }

    private List<FixtureDTO> createFixtures(Collection<Duel> duels) {
        return duels.stream()
                .map(duel -> {
                    var dto = new FixtureDTO();

                    dto.setHomePlayer(duel.getHomePlayer());
                    dto.setAwayPlayer(duel.getAwayPlayer());
                    dto.setHomePlayerPoints(duel.getHomePlayerPoints());
                    dto.setAwayPlayerPoints(duel.getAwayPlayerPoints());
                    dto.setDuelResult(duel.getResult());
                    dto.setTimestamp(duel.getTimestamp());

                    return dto;
                })
                .collect(Collectors.toList());
    }
}
