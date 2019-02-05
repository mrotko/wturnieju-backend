package pl.wturnieju.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.repository.TournamentTableRepository;
import pl.wturnieju.tournament.system.table.TournamentTable;

@Service
@RequiredArgsConstructor
public class TournamentTableService implements ITournamentTableService {

    private final TournamentTableRepository repository;

    @Override
    public TournamentTable getByGroupId(String groupId) {
        return repository.findById(groupId).orElse(null);
    }

    @Override
    public void deleteByGroupId(String groupId) {
        repository.deleteById(groupId);
    }

    @Override
    public TournamentTable insert(TournamentTable tournamentTable) {
        return repository.insert(tournamentTable);
    }

    @Override
    public TournamentTable update(TournamentTable tournamentTable) {
        return repository.save(tournamentTable);
    }
}
