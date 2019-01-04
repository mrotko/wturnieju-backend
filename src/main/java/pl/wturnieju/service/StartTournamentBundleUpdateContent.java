package pl.wturnieju.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.model.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
public class StartTournamentBundleUpdateContent extends TournamentBundleUpdateContent {

    private Timestamp startDate;

    public StartTournamentBundleUpdateContent() {
        setType(TournamentBundleUpdateContentType.START);
    }
}
