package pl.wturnieju.model.generic;

import lombok.Data;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentSystemType;

@Data
public abstract class GenericFixtureUpdateBundle {

    private String changedById;

    private Timestamp timestamp;

    private String tournamentId;

    private String fixtureId;

    private TournamentSystemType type;

    private BundleUpdateContent content;
}
