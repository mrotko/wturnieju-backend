package pl.wturnieju.service;

import lombok.Data;
import pl.wturnieju.model.Timestamp;

@Data
public class EndTournamentBundleUpdateContent extends TournamentBundleUpdateContent {

    private Timestamp endDate;

    public EndTournamentBundleUpdateContent() {
        setType(TournamentBundleUpdateContentType.END);
    }


}
