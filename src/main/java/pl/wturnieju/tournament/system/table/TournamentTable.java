package pl.wturnieju.tournament.system.table;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentTable {

    private List<TournamentTableRow> rows = new ArrayList<>();
}
