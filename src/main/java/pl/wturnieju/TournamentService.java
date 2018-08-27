package pl.wturnieju;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.generic.Tournament;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public List<Tournament> getAllActive() {
        return Stream.of(
                tournamentRepository.getAllByStatus(TournamentStatus.IN_PROGRESS),
                tournamentRepository.getAllByStatus(TournamentStatus.BEFORE_START))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
