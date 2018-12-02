package pl.wturnieju.dto.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import pl.wturnieju.dto.TournamentTableDTO;
import pl.wturnieju.dto.TournamentTableRowDTO;
import pl.wturnieju.model.generic.GenericTournamentTable;
import pl.wturnieju.model.generic.SwissTournamentTable;

public class TournamentTableMapping {

    private static Map<String, Function<GenericTournamentTable, TournamentTableDTO>> mappingStrategy = new HashMap<>();

    public static TournamentTableDTO map(GenericTournamentTable table) {
        if (table == null) {
            return null;
        }
        return mappingStrategy.computeIfAbsent(
                table.getClass().getSimpleName(), TournamentTableMapping::getMappingStrategy).apply(table);
    }

    static Function<GenericTournamentTable, TournamentTableDTO> getMappingStrategy(String simpleClassName) {
        if (simpleClassName.equals(SwissTournamentTable.class.getSimpleName())) {
            return (table -> {
                var tableDto = new TournamentTableDTO();
                ((SwissTournamentTable) table).getFinalStandings().forEach(row -> {
                    var rowDto = new TournamentTableRowDTO();

                    rowDto.setPosition(tableDto.getRows().size() + 1);
                    rowDto.setProfileId(row.getProfileId());
                    rowDto.setWins(row.getWins());
                    rowDto.setDraws(row.getDraws());
                    rowDto.setLoses(row.getLoses());
                    rowDto.setWins(row.getWins());
                    rowDto.setPoints(row.getPoints());
                    rowDto.setSmallPoints(row.getSmallPoints());

                    tableDto.getRows().add(rowDto);
                });
                return tableDto;
            });
        }

        throw new IllegalArgumentException("Unknown tournament table [" + simpleClassName + "]");
    }
}
