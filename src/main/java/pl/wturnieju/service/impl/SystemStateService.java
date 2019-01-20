package pl.wturnieju.service.impl;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.repository.SystemStateRepository;
import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.system.state.SystemState;

@RequiredArgsConstructor
public abstract class SystemStateService<T extends SystemState> implements ISystemStateService<T> {

    private final SystemStateRepository<T> repository;

    @Override
    public void insertSystemState(T state) {
        state.setLastUpdate(Timestamp.now());
        repository.insert(state);
    }

    @Override
    public T getSystemStateByTournamentId(String tournamentId) {
        return repository.getByTournamentId(tournamentId);
    }

    @Override
    public T updateSystemState(T state) {
        return repository.save(state);
    }
}
