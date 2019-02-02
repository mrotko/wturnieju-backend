package pl.wturnieju.service.impl;

import org.springframework.stereotype.Service;

import pl.wturnieju.repository.SystemStateRepository;
import pl.wturnieju.tournament.system.state.LeagueSystemState;

@Service
public class LeagueSystemStateService extends SystemStateService<LeagueSystemState> {
    public LeagueSystemStateService(SystemStateRepository<LeagueSystemState> repository) {
        super(repository);
    }
}
