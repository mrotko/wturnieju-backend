package pl.wturnieju.controller.dto.tournament.table;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import pl.wturnieju.tournament.system.table.TournamentTable;

@Mapper(componentModel = "spring", uses = {TournamentTableRowDtoMapper.class})
public interface TournamentTableDtoMapper {

    TournamentTableDto tournamentTableToTournamentTableDto(TournamentTable table);

    @AfterMapping
    default TournamentTableDto setOrderFields(@MappingTarget TournamentTableDto dto) {
        int counter = 0;
        for (TournamentTableRowDto row : dto.getRows()) {
            row.setBaseOrderNum(counter++);
        }
        return dto;
    }
}
