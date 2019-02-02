package pl.wturnieju.service.impl;

import org.springframework.stereotype.Service;

import pl.wturnieju.repository.SystemStateRepository;
import pl.wturnieju.tournament.system.state.CustomSystemState;

@Service
public class CustomSystemStateService extends SystemStateService<CustomSystemState> {
    public CustomSystemStateService(SystemStateRepository<CustomSystemState> repository) {
        super(repository);
    }
}
