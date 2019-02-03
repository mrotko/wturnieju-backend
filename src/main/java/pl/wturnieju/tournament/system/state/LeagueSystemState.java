package pl.wturnieju.tournament.system.state;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.gamefixture.LeagueGameFixture;

@Data
@EqualsAndHashCode(callSuper = true)
public class LeagueSystemState extends SystemState<LeagueGameFixture> {

    private List<ImmutablePair<String, String>> pairsAfterFirstRound = new ArrayList<>();
}
