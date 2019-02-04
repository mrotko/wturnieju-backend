package pl.wturnieju.controller.dto.tournament;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Participant;

@Mapper(componentModel = "spring")
public abstract class ParticipantDtoMapper {

    @Autowired
    protected IParticipantService participantService;

    @Mapping(source = "memberIds", target = "members")
    public abstract ParticipantDto participantToParticipantDto(Participant participant);

    public ParticipantDto participantIdToParticipantDto(String participantId) {
        var participant = participantService.getById(participantId);
        return participantToParticipantDto(participant);
    }

    public abstract List<ParticipantDto> participantIdsToParticipantDtos(List<String> participantIds);
}
