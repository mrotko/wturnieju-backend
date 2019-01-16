package pl.wturnieju.controller.dto.tournament.table;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableRow;

@Mapper(componentModel = "spring", uses = {TournamentTableRowDtoMapper.class})
public interface TournamentTableDtoMapper {

    @Mapping(source = "tournamentId", target = "tournamentId")
    @Mapping(source = "table.rows", target = "rows")
    TournamentTableDto tournamentTableToTournamentTableDto(
            String tournamentId,
            TournamentTable<TournamentTableRow> table);

    @AfterMapping
    default TournamentTableDto setOrderFields(@MappingTarget TournamentTableDto dto) {
        int counter = 0;
        for (TournamentTableRowDto row : dto.getRows()) {
            row.setBaseOrderNum(counter++);
        }
        return dto;
    }
}
