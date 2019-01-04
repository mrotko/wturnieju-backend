package pl.wturnieju.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.model.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
public class EndTournamentBundleUpdateContent extends TournamentBundleUpdateContent {

    private Timestamp endDate;

    public EndTournamentBundleUpdateContent() {
        setType(TournamentBundleUpdateContentType.END);
    }


}
