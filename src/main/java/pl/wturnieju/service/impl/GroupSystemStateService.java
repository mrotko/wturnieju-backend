package pl.wturnieju.service.impl;

import org.springframework.stereotype.Service;

import pl.wturnieju.repository.SystemStateRepository;
import pl.wturnieju.tournament.system.state.GroupSystemState;

@Service
public class GroupSystemStateService extends SystemStateService<GroupSystemState> {
    public GroupSystemStateService(SystemStateRepository<GroupSystemState> repository) {
        super(repository);
    }
}
