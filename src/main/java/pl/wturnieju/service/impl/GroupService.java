package pl.wturnieju.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.repository.GroupRepository;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.tournament.system.state.Group;

@Service
@RequiredArgsConstructor
public class GroupService implements IGroupService {

    private final GroupRepository repository;

    @Override
    public Group getById(String groupId) {
        return repository.findById(groupId).orElse(null);
    }

    @Override
    public Group insert(Group group) {
        return repository.insert(group);
    }
}
