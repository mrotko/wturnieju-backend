package pl.wturnieju.service;

import pl.wturnieju.tournament.system.state.SystemState;

public interface ISystemStateService {

    void insertSystemState(SystemState state);

    SystemState getSystemStateByTournamentId(String tournamentId);

    SystemState updateSystemState(SystemState state);
}
