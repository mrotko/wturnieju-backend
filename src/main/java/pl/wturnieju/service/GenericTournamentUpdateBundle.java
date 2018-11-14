package pl.wturnieju.service;

import lombok.Data;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.Timestamp;

@Data
public abstract class GenericTournamentUpdateBundle {

    private Timestamp timestamp;

    private IProfile changedBy;

    private String tournamentId;

    private TournamentBundleUpdateContent content;

}
