package pl.wturnieju.service.impl;

import org.springframework.stereotype.Service;

import pl.wturnieju.repository.SystemStateRepository;
import pl.wturnieju.tournament.system.state.KnockOutSystemState;

@Service
public class KnockOutSystemStateService extends SystemStateService<KnockOutSystemState> {
    public KnockOutSystemStateService(SystemStateRepository<KnockOutSystemState> repository) {
        super(repository);
    }
}
