package pl.wturnieju.model.generic;

import java.time.LocalDateTime;

import lombok.Data;
import pl.wturnieju.model.IProfile;

@Data
public abstract class GenericFixtureUpdateBundle {

    private IProfile changedBy;

    private LocalDateTime timestamp;

    private String tournamentId;

    private String fixtureId;

    private BundleUpdateContent content;
}
