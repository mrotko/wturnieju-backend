package pl.wturnieju.controller.dto.table;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentTableDTO {

    private List<ColumnType> columns;

    private List<TournamentTableRowDTO> rows;
    
}
