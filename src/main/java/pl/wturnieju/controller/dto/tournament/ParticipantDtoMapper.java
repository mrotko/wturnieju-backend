package pl.wturnieju.controller.dto.tournament;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.wturnieju.tournament.Participant;

@Mapper(componentModel = "spring")
public interface ParticipantDtoMapper {

    @Mapping(source = "participant.id", target = "id")
    @Mapping(source = "participant.participantStatus", target = "participantStatus")
    @Mapping(source = "participant.invitationStatus", target = "invitationStatus")
    @Mapping(source = "participant.participantType", target = "participantType")
    @Mapping(source = "participant.name", target = "name")
    ParticipantDto participantToParticipantDto(Participant participant);
}
