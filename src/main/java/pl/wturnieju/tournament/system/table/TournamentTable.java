package pl.wturnieju.tournament.system.table;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentTable {

    @Id
    private String groupId;

    private String name;

    private List<TournamentTableRow> rows = new ArrayList<>();
}
