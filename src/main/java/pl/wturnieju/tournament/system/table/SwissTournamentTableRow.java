package pl.wturnieju.tournament.system.table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SwissTournamentTableRow extends TournamentTableRow {

    public SwissTournamentTableRow(String teamId, String name) {
        super(teamId, name);
    }
}
