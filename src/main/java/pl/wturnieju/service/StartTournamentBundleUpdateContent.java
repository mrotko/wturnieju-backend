package pl.wturnieju.service;

import lombok.Data;
import pl.wturnieju.model.Timestamp;

@Data
public class StartTournamentBundleUpdateContent extends TournamentBundleUpdateContent {

    private Timestamp startDate;

    public StartTournamentBundleUpdateContent() {
        setType(TournamentBundleUpdateContentType.START);
    }
}
