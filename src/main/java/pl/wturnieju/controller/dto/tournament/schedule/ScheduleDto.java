package pl.wturnieju.controller.dto.tournament.schedule;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDto {

    private String tournamentId;

    private String tournamentName;

    private Integer round;

    private List<ScheduleElementDto> elements;
}
