package pl.wturnieju.controller.dto.tournament.table;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentTableDto {

    private String groupId;

    private String name;

    private List<TournamentTableRowDto> rows;
}
