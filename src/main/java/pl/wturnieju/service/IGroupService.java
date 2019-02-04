package pl.wturnieju.service;

import pl.wturnieju.tournament.system.state.Group;

public interface IGroupService {

    Group getById(String groupId);

    Group insert(Group group);
}
