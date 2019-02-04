package pl.wturnieju.controller.dto.tournament;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.tournament.StageType;

@Getter
@Setter
public class GroupDto {

    private String id;

    private String name;

    private String tournamentId;

    private List<ParticipantDto> participants;

    private StageType stageType;
}
