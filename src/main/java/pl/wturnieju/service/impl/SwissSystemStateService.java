package pl.wturnieju.service.impl;

import org.springframework.stereotype.Service;

import pl.wturnieju.repository.SystemStateRepository;
import pl.wturnieju.tournament.system.state.SwissSystemState;

@Service
public class SwissSystemStateService extends SystemStateService<SwissSystemState> {

    public SwissSystemStateService(SystemStateRepository<SwissSystemState> repository) {
        super(repository);
    }
}
