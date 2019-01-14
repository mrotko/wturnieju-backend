package pl.wturnieju.controller.dto.tournament;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import pl.wturnieju.model.User;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.tournament.Participant;

@Mapper(componentModel = "spring")
public abstract class TournamentParticipantDtoMapper {

    @Autowired
    protected IUserService userService;

    public TournamentParticipantDto participantToTournamentParticipantDto(Participant participant) {
        return participantToTournamentParticipantDto(getUser(participant), participant);
    }

    @Mapping(source = "participant.id", target = "id")
    @Mapping(source = "participant.participantStatus", target = "status")
    @Mapping(source = "participant.invitationStatus", target = "invitationStatus")
    @Mapping(source = "participant.name", target = "name")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.username", target = "email")
    protected abstract TournamentParticipantDto participantToTournamentParticipantDto(User user,
            Participant participant);

    protected User getUser(Participant participant) {
        return userService.getUserById(participant.getId());
    }
}
