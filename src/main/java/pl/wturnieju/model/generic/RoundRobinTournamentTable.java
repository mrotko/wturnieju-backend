package pl.wturnieju.model.generic;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoundRobinTournamentTable extends GenericTournamentTable<RoundRobinTournamentTableRow> {
}
