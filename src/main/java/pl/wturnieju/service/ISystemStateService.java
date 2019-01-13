package pl.wturnieju.service;

import pl.wturnieju.tournament.system.state.SystemState;

public interface ISystemStateService<T extends SystemState> {

    void insertSystemState(T state);

    T getSystemStateByTournamentId(String tournamentId);

    T updateSystemState(T state);
}
