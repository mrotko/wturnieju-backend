package pl.wturnieju.service.impl;

import org.springframework.stereotype.Service;

import pl.wturnieju.repository.SystemStateRepository;
import pl.wturnieju.tournament.system.state.RoundRobinSystemState;

@Service
public class RoundRobinSystemStateService extends SystemStateService<RoundRobinSystemState> {

    public RoundRobinSystemStateService(SystemStateRepository<RoundRobinSystemState> repository) {
        super(repository);
    }
}
