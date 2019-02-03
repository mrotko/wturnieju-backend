package pl.wturnieju.tournament.system.state;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LeagueSystemState extends SystemState {

    private List<ImmutablePair<String, String>> pairsAfterFirstRound = new ArrayList<>();
}
