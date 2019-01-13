package pl.wturnieju.controller.dto.tournament.table;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableRow;

@Mapper(componentModel = "spring", uses = {TournamentTableRowDtoMapper.class})
public interface TournamentTableDtoMapper {

    @Mapping(source = "tournamentId", target = "tournamentId")
    @Mapping(source = "table.rows", target = "rows")
    TournamentTableDto tournamentTableToTournamentTableDto(
            String tournamentId,
            TournamentTable<TournamentTableRow> table);
}
