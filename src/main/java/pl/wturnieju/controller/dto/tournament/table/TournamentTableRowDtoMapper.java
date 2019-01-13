package pl.wturnieju.controller.dto.tournament.table;

import org.mapstruct.Mapper;

import pl.wturnieju.tournament.system.table.TournamentTableRow;

@Mapper(componentModel = "spring")
public interface TournamentTableRowDtoMapper {

    TournamentTableRowDto tournamentTableRowToTournamentTableRowDto(TournamentTableRow source);
}
