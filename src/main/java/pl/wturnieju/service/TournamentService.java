package pl.wturnieju.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.repository.TournamentRepository;

@Service
@RequiredArgsConstructor
public class TournamentService implements ITournamentService {

    private final TournamentRepository tournamentRepository;

    @Override
    public void update(Tournament tournament) {
        if (tournament != null) {
            tournamentRepository.save(tournament);
        }
    }

    @Override
    public List<Tournament> getAllActive() {
        return Stream.of(
                tournamentRepository.getAllByStatus(TournamentStatus.IN_PROGRESS),
                tournamentRepository.getAllByStatus(TournamentStatus.BEFORE_START))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Tournament> getById(String tournamentId) {
        return tournamentRepository.findById(tournamentId);
    }
}
