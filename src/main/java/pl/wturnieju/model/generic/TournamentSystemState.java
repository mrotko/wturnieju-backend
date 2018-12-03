package pl.wturnieju.model.generic;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.swiss.SystemParticipant;
import pl.wturnieju.service.GenericTournamentUpdateBundle;


@RequiredArgsConstructor
@Data
public abstract class TournamentSystemState extends Persistent {

    private int currentRound;

    private Timestamp lastUpdate;

    private List<GenericFixtureUpdateBundle> updateFixtureBundles = new ArrayList<>();

    private List<GenericTournamentUpdateBundle> updateTournamentBundles = new ArrayList<>();

    private GenericTournamentTable tournamentTable;

    private List<Fixture> fixtures = new ArrayList<>();

    protected List<SystemParticipant> participants = new ArrayList<>();
}
