package pl.wturnieju.service.impl;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.repository.SystemStateRepository;
import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.system.state.SystemState;

@RequiredArgsConstructor
public class SystemStateService implements ISystemStateService {

    private final SystemStateRepository repository;

    @Override
    public void insertSystemState(SystemState state) {
        state.setLastUpdate(Timestamp.now());
        repository.insert(state);
    }

    @Override
    public SystemState getSystemStateByTournamentId(String tournamentId) {
        return repository.getByTournamentId(tournamentId);
    }

    @Override
    public SystemState updateSystemState(SystemState state) {
        return repository.save(state);
    }
}
