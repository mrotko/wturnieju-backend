package pl.wturnieju.service;

import java.time.LocalDateTime;

import lombok.Data;
import pl.wturnieju.model.IProfile;

@Data
public abstract class GenericTournamentUpdateBundle {

    private LocalDateTime timestamp;

    private IProfile changedBy;

    private String tournamentId;

    private TournamentBundleUpdateContent content;

}
